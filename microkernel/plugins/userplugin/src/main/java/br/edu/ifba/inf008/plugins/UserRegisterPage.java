package br.edu.ifba.inf008.plugins;

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
public class UserRegisterPage extends VBox {

    // Declara os componentes da UI como variáveis de instância
    // para que possam ser acessados por outros métodos.
    private TextField nameField;
    private TextField emailField;
    private Button saveButton;
    private Label resultLabel;
    private UserManager userManager;

    public UserRegisterPage(UserManager userManager) {
        this.userManager = userManager;
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20)); // Espaçamento interno
        this.setSpacing(10); // Espaçamento entre os componentes

        // 2. Cria e adiciona um título para a tela
        Label titleLabel = new Label("Cadastro de Usuário");
        titleLabel.setFont(new Font("Arial", 22));

        // 3. Cria os campos e seus rótulos
        Label nameLabel = new Label("Nome:");
        nameField = new TextField();
        nameField.setPromptText("Digite o nome completo"); // Texto de ajuda

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("exemplo@email.com");

        // 4. Cria o botão de ação
        saveButton = new Button("Salvar");

        resultLabel = new Label("");

        // 5. Adiciona todos os componentes criados ao painel VBox na ordem desejada
        this.getChildren().addAll(
                titleLabel,
                nameLabel,
                nameField,
                emailLabel,
                emailField,
                resultLabel,
                saveButton);

        // 6. Define a ação para o clique do botão "Salvar"
        setupActions();
    }

    private void setupActions() {
        saveButton.setOnAction(event -> {
            // Pega os valores dos campos de texto
            String name = nameField.getText();
            String email = emailField.getText();

            // Validação simples para não salvar em branco
            if (name.isEmpty() || email.isEmpty()) {
                System.out.println("Ação 'Salvar': Erro! Nome e Email são obrigatórios.");
                return; // Interrompe a execução
            }
            // Aqui chamo a lógica de persistência no banco de dados
            try {
                int newUserId = userManager.saveNewUser(name, email);

                resultLabel.setText("Usuário '" + name + ", de ID '" + newUserId + "' cadastrado com sucesso!");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Usuário '" + name + "' cadastrado com sucesso!");
                alert.showAndWait();
            } catch (Exception e) {
                resultLabel.setText("Erro ao salvar o usuário: " + e.getMessage());

                System.err.println("Falha ao salvar usuário no banco de dados.");
                e.printStackTrace(); // Imprime o erro detalhado no console

                // Mostra um alerta de erro para o usuário
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Banco de Dados");
                alert.setHeaderText("Não foi possível salvar o usuário.");
                alert.setContentText("Ocorreu um erro ao se comunicar com o banco de dados.");
                alert.showAndWait();
            }

            System.out.println("Ação 'Salvar':");
            System.out.println("  - Nome: " + name);
            System.out.println("  - Email: " + email);

            // Limpa os campos após salvar, para um novo cadastro
            clearFields();
        });
    }

    /**
     * Limpa os campos de texto para permitir um novo cadastro.
     */
    public void clearFields() {
        nameField.clear();
        emailField.clear();
    }
}