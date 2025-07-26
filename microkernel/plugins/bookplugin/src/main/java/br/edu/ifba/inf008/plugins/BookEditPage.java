package br.edu.ifba.inf008.plugins;

import java.sql.SQLException;
import java.util.Optional;

import br.edu.ifba.inf008.plugins.model.Book;
import br.edu.ifba.inf008.plugins.persistence.BookManager;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Representa a tela de UI para o gerenciamento de books.
 * Contém os campos para entrada de dados e o botão de ação.
 */
public class BookEditPage extends VBox {

    // Declara os componentes da UI como variáveis de instância
    // para que possam ser acessados por outros métodos.
    private TextField idField;
    private TextField titleField;
    private TextField authorField;
    private TextField isbnField;
    private TextField publishedYearField;
    private TextField copiesAvailableField;

    private Button searchButton;
    private Button saveButton;
    private Button deleteButton;
    private Label resultLabel;
    private BookManager bookManager;
    private int idToSearch;

    public BookEditPage(BookManager bookManager) {
        this.bookManager = bookManager;
        this.idToSearch = -1;
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20)); // Espaçamento interno
        this.setSpacing(10); // Espaçamento entre os componentes

        // 2. Cria e adiciona um título para a tela
        Label title = new Label("Editar Livro");
        title.setFont(new Font("Arial", 22));

        // 3. Pede o ID do livro a ser editado

        Label idLabel = new Label("ID:");
        idField = new TextField();
        idField.setPromptText("Digite o ID do livro que deseja editar: ");

        searchButton = new Button("Buscar");

        // 4. Cria os campos e seus rótulos
        Label titleLabel = new Label("Título:");
        titleField = new TextField();
        titleField.setPromptText("Digite o título completo"); // Texto de ajuda
        titleField.setDisable(true);

        Label authorLabel = new Label("author:");
        authorField = new TextField();
        authorField.setPromptText("Ex: Machado de Assis...");
        authorField.setDisable(true);

        Label isbnLabel = new Label("isbn:");
        isbnField = new TextField();
        isbnField.setPromptText("123...");
        isbnField.setDisable(true);

        Label publishedYearLabel = new Label("publishedYear:");
        publishedYearField = new TextField();
        publishedYearField.setPromptText("Ex: 2019");
        publishedYearField.setDisable(true);

        Label copiesAvailableLabel = new Label("Cópias disponíveis:");
        copiesAvailableField = new TextField();
        copiesAvailableField.setPromptText("Ex: 2019");
        copiesAvailableField.setDisable(true);

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
                title,
                idLabel,
                idField,
                searchButton,
                titleLabel,
                titleField,
                authorLabel,
                authorField,
                isbnLabel,
                isbnField,
                publishedYearLabel,
                publishedYearField,
                copiesAvailableLabel,
                copiesAvailableField, saveButton,
                deleteButton,
                resultLabel);

        // 7. Define a ação para o clique dos botões "Salvar"
        setupActions();
    }

    private void setupActions() {
        searchButton.setOnAction(event -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Aviso", idText);
                return;
            }

            try {
                this.idToSearch = Integer.parseInt(idText);

                Optional<Book> foundUserOptional = bookManager.getBookById(idToSearch);
                foundUserOptional.ifPresentOrElse(
                        // Ação a ser executada SE o livro for encontrado:
                        book -> {
                            // Preenche os campos com os dados do livro encontrado.
                            titleField.setText(book.getTitle());
                            authorField.setText(book.getAuthor());
                            isbnField.setText(book.getIsbn());
                            publishedYearField.setText(String.valueOf(book.getPublicationDate()));
                            copiesAvailableField.setText(String.valueOf(book.getCopiesAvailable()));

                            // Habilita os campos para edição.
                            titleField.setDisable(false);
                            authorField.setDisable(false);
                            isbnField.setDisable(false);
                            publishedYearField.setDisable(false);
                            copiesAvailableField.setDisable(false);

                            saveButton.setDisable(false); // Habilita o botão de salvar
                            deleteButton.setDisable(false); // Habilita o botão de deletar

                            resultLabel.setText("Livro encontrado. Edite os dados e clique em salvar.");
                            resultLabel.setStyle("-fx-text-fill: green;");
                        },
                        // Ação a ser executada SE o livro NÃO for encontrado:
                        () -> {
                            showAlert(Alert.AlertType.INFORMATION, "Não Encontrado",
                                    "Nenhum livro encontrado com o ID: " + idToSearch);
                            clearEditableFields();
                        });
            } catch (NumberFormatException e) {
                // Este bloco é executado se o usuário digitar algo que não é um número.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Entrada Inválida", "O ID deve ser um número inteiro válido.");
            } catch (SQLException e) {
                // Este bloco é executado se ocorrer qualquer outra exceção.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao buscar o livro.");
            }
        });

        saveButton.setOnAction(event -> {
            // Pega os valores dos campos de texto
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            int publishedYear = Integer.parseInt(publishedYearField.getText());
            int copiesAvailable = Integer.parseInt(copiesAvailableField.getText());

            // Validação simples para não salvar em branco
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publishedYear < 0 || copiesAvailable < 0 || idToSearch == -1) {
                System.out.println("Ação 'Salvar': Erro! por favor, preencha todos os campos.");
                return; // Interrompe a execução
            }

            try {
                bookManager.updateBook(idToSearch, title, author, isbn, publishedYear, copiesAvailable);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Livro atualizado com sucesso!");
                clearEditableFields();
            } catch (SQLException e) {
                // Este bloco é executado se ocorrer qualquer exceção SQL.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro SQL ao atualizar o livro.");
            }
            clearEditableFields();

            titleField.setDisable(true);
            authorField.setDisable(true);
            isbnField.setDisable(true);
            publishedYearField.setDisable(true);
            copiesAvailableField.setDisable(true);
        });

        deleteButton.setOnAction(event -> {
            try {

                bookManager.deleteBook(idToSearch);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Livro excluído com sucesso!");
                clearEditableFields();
            } catch (SQLException e) {
                // Este bloco é executado se ocorrer qualquer exceção SQL.
                clearEditableFields();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao excluir o livro.");
            }
        });

    }

    /**
     * Limpa os campos de texto para permitir um novo cadastro.
     */
    public void clearEditableFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        publishedYearField.clear();
        copiesAvailableField.clear();
        resultLabel.setText("");
        // Desabilita os campos novamente para forçar uma nova busca.
        titleField.setDisable(true);
        authorField.setDisable(true);
        isbnField.setDisable(true);
        publishedYearField.setDisable(true);
        copiesAvailableField.setDisable(true);
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