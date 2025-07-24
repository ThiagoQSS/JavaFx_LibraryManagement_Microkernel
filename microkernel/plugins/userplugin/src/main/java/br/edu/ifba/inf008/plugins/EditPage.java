package br.edu.ifba.inf008.plugins;

import javafx.geometry.Insets;
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

    public EditPage() {
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
        saveButton = new Button("Salvar");

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
            saveButton
        );

        // 7. Define a ação para o clique do botão "Salvar"
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

            // Por enquanto, vamos apenas imprimir os dados no console
            // No futuro, aqui você chamaria a lógica de persistência no banco de dados
            System.out.println("Ação 'Editar':");
            System.out.println("  - Novo Nome: " + name);
            System.out.println("  - Novo Email: " + email);

            // Limpa os campos após salvar, para um novo cadastro
            clearFields();

            nameField.setDisable(true);
            emailField.setDisable(true);
        });

        searchButton.setOnAction(event -> {
            // Here it Should get the INFO of the user to be edited
            // [...]

            // Set the nameField and emailField with the info of the user
            nameField.setText("Thiago Quadros");
            emailField.setText("tqss2759@gmail.com");
            // Make'em editable
            nameField.setDisable(false);
            emailField.setDisable(false);
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