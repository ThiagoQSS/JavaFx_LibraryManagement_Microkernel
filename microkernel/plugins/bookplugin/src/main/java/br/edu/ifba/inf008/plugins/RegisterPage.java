package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.plugins.persistence.BookManager;
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
public class RegisterPage extends VBox {

    // Declara os componentes da UI como variáveis de instância
    // para que possam ser acessados por outros métodos.
    private TextField titleField;
    private TextField authorField;
    private TextField isbnField;
    private TextField publishedYearField;
    private TextField copiesAvailableField;

    private Button saveButton;
    private Label resultLabel;
    private BookManager userManager;

    public RegisterPage(BookManager userManager) {
        this.userManager = userManager;
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20)); // Espaçamento interno
        this.setSpacing(10); // Espaçamento entre os componentes

        // 2. Cria e adiciona um título para a tela
        Label title = new Label("Cadastro de Livros");
        title.setFont(new Font("Arial", 22));

        // 3. Cria os campos e seus rótulos
        Label titleLabel = new Label("Nome:");
        titleField = new TextField();
        titleField.setPromptText("Digite o nome completo"); // Texto de ajuda

        Label emailLabel = new Label("Email:");
        authorField = new TextField();
        authorField.setPromptText("exemplo@email.com");

        Label isbnLabel = new Label("ISBN:");
        isbnField = new TextField();
        isbnField.setPromptText("Digite o ISBN");

        Label publishedYearLabel = new Label("Ano de publicação:");
        publishedYearField = new TextField();
        publishedYearField.setPromptText("Digite o ano de publicação");

        Label copiesAvailableLabel = new Label("Copias disponíveis:");
        copiesAvailableField = new TextField();
        copiesAvailableField.setPromptText("Digite o número de copias disponíveis");

        // 4. Cria o botão de ação
        saveButton = new Button("Salvar");

        resultLabel = new Label("");

        // 5. Adiciona todos os componentes criados ao painel VBox na ordem desejada
        this.getChildren().addAll(
                title,
                titleLabel, titleField,
                emailLabel, authorField,
                isbnLabel, isbnField,
                publishedYearLabel, publishedYearField,
                copiesAvailableLabel, copiesAvailableField,
                saveButton, resultLabel
                );

        // 6. Define a ação para o clique do botão "Salvar"
        setupActions();
    }

    private void setupActions() {
        saveButton.setOnAction(event -> {
            // Pega os valores dos campos de texto
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            int publishedYear = Integer.parseInt(publishedYearField.getText());
            int copiesAvailable = Integer.parseInt(copiesAvailableField.getText());

            // Validação simples para não salvar em branco
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publishedYear == 0 || copiesAvailable == 0) {
                System.out.println("Ação 'Salvar': Erro! title, author, isbn, publishedYear e copiesAvailable são obrigatórios.");
                return; // Interrompe a execução
            }
            // Aqui chamo a lógica de persistência no banco de dados
            try {
                int newBookId = userManager.saveNewBook(title, author, isbn, publishedYear, copiesAvailable);

                resultLabel.setText("Usuário '" + title + ", de ID '" + newBookId + "' cadastrado com sucesso!");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Usuário '" + title + "' cadastrado com sucesso!");
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

            // Limpa os campos após salvar, para um novo cadastro
            clearFields();
        });
    }

    /**
     * Limpa os campos de texto para permitir um novo cadastro.
     */
    public void clearFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        publishedYearField.clear();
        copiesAvailableField.clear();
    }
}