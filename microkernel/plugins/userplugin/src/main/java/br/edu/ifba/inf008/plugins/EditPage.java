package br.edu.ifba.inf008.plugins;

import java.sql.SQLException;
import java.util.Optional;

import br.edu.ifba.inf008.plugins.model.User;
import br.edu.ifba.inf008.plugins.persistence.UserManager;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Representa a tela de UI para o gerenciamento de Usuários.
 * Contém os campos para entrada de dados e o botão de ação.
 */
public class EditPage extends VBox {

    // Declara os componentes da UI como variáveis de instância
    // para que possam ser acessados por outros métodos.
    private TextField nameField;
    private TextField emailField;
    private TextField idField;
    private Button searchButton;
    private Button saveButton;
    private Button deleteButton;
    private Label resultLabel;
    private UserManager userManager;
    private int idToSearch;

    public EditPage(UserManager userManager) {
        this.userManager = userManager;
        this.idToSearch = -1;
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20)); // Espaçamento interno
        this.setSpacing(10); // Espaçamento entre os componentes

        // 2. Cria e adiciona um título para a tela
        Label titleLabel = new Label("Editar Usuário");
        titleLabel.setFont(new Font("Arial", 22));

        // 3. Pede o ID do usuário a ser editado

        Label idLabel = new Label("ID:");
        idField = new TextField();
        idField.setPromptText("Digite o ID do usuário que deseja editar: ");

        searchButton = new Button("Buscar");

        // 4. Cria os campos e seus rótulos
        Label nameLabel = new Label("Nome:");
        nameField = new TextField();
        nameField.setPromptText("Digite o nome completo"); // Texto de ajuda
        nameField.setDisable(true);

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("exemplo@email.com");
        emailField.setDisable(true);

        // 5. Cria o botão de ação
        saveButton = new Button("Salvar Alterações");
        saveButton.setDisable(true);
        deleteButton = new Button("Excluir Usuário");
        deleteButton.setStyle("-fx-border-width: 1; -fx-border-color: red;");
        deleteButton.setDisable(true);

        // 5.5 Cria o label para exibir resultados
        resultLabel = new Label("");

        // 6. Adiciona todos os componentes criados ao painel VBox na ordem desejada
        this.getChildren().addAll(
                titleLabel,
                idLabel,
                idField,
                searchButton,
                nameLabel,
                nameField,
                emailLabel,
                emailField,
                saveButton,
                deleteButton,
                resultLabel);

        // 7. Define a ação para o clique do botão "Salvar"
        setupActions();
    }

    private void setupActions() {
        searchButton.setOnAction(event -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atenção");
                alert.setHeaderText(null);
                alert.setContentText("Digite um id para buscar o usuário!");
                alert.showAndWait();
                return;
            }

            try {
                this.idToSearch = Integer.parseInt(idText);

                Optional<User> foundUserOptional = userManager.getUserById(idToSearch);
                foundUserOptional.ifPresentOrElse(
                        // Ação a ser executada SE o usuário for encontrado:
                        user -> {
                            // Preenche os campos com os dados do usuário encontrado.
                            nameField.setText(user.getName());
                            emailField.setText(user.getEmail());

                            // Habilita os campos para edição.
                            nameField.setDisable(false);
                            emailField.setDisable(false);
                            saveButton.setDisable(false); // Habilita o botão de salvar
                            deleteButton.setDisable(false); // Habilita o botão de deletar

                            resultLabel.setText("Usuário encontrado. Edite os dados e clique em salvar.");
                            resultLabel.setStyle("-fx-text-fill: green;");
                        },
                        // Ação a ser executada SE o usuário NÃO for encontrado:
                        () -> {
                            showAlert(Alert.AlertType.INFORMATION, "Não Encontrado",
                                    "Nenhum usuário encontrado com o ID: " + idToSearch);
                            clearEditableFields();
                        });
            } catch (NumberFormatException e) {
                // Este bloco é executado se o usuário digitar algo que não é um número.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Entrada Inválida", "O ID deve ser um número inteiro válido.");
            } catch (SQLException e) {
                // Este bloco é executado se ocorrer qualquer outra exceção.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao buscar o usuário.");
            }
        });

        saveButton.setOnAction(event -> {
            // Pega os valores dos campos de texto
            String name = nameField.getText();
            String email = emailField.getText();

            // Validação simples para não salvar em branco
            if (name.isEmpty() || email.isEmpty() || idToSearch == -1) {
                System.out.println("Ação 'Salvar': Erro! Nome, Email e o ID de busca são obrigatórios.");
                return; // Interrompe a execução
            }

            try {
                userManager.updateUser(idToSearch, name, email);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário atualizado com sucesso!");
                clearEditableFields();
            } catch (SQLException e) {
                // Este bloco é executado se ocorrer qualquer exceção SQL.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao atualizar o usuário.");
            }
            clearEditableFields();

            nameField.setDisable(true);
            emailField.setDisable(true);
        });

        deleteButton.setOnAction(event -> {
            try {

                userManager.deleteUser(idToSearch);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário excluído com sucesso!");
                clearEditableFields();
            } catch (SQLException e) {
                // Este bloco é executado se ocorrer qualquer exceção SQL.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao excluir o usuário.");
            }
        });

    }

    /**
     * Limpa os campos de texto para permitir um novo cadastro.
     */
    public void clearEditableFields() {
        nameField.clear();
        emailField.clear();
        resultLabel.setText("");
        // Desabilita os campos novamente para uma nova busca.
        nameField.setDisable(true);
        emailField.setDisable(true);
        saveButton.setDisable(true);
        deleteButton.setDisable(true);

        idToSearch = -1;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}