package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.plugins.model.Book;
import br.edu.ifba.inf008.plugins.persistence.BookManager;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Representa a página que exibe uma lista de todos os livros cadastrados.
 */
public class BookListPage extends VBox {

    private TableView<Book> userTable;

    public BookListPage(BookManager bookManager) {
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20));
        this.setSpacing(10);

        // 2. Cria e adiciona um título para a página, cria os inputs no metodo privado
        Label titleLabel = new Label("Lista de livros");
        titleLabel.setFont(new Font("Arial", 22));
        titleLabel.setStyle("-fx-padding: 5px");

        ObservableList<Book> masterUserList = bookManager.getBookList();
        FilteredList<Book> filteredUserList = new FilteredList<>(masterUserList, p -> true);

        VBox searchContainer = createSearchContainer(filteredUserList);
        // 3. Configura a Tabela
        setupTable(filteredUserList);

        // 4. Adiciona os componentes ao layout
        this.getChildren().addAll(titleLabel, searchContainer, userTable);
    }

    private void setupTable(FilteredList<Book> listToDisplay) {
        // Cria a Tabela
        userTable = new TableView<>();

        // Cria as Colunas
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Book, String> titleColumn = new TableColumn<>("Título");
        TableColumn<Book, String> authorColumn = new TableColumn<>("author");
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        TableColumn<Book, Integer> copiesColumn = new TableColumn<>("Copias");
        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Ano de publicação");

        // Conecta as colunas com as propriedades do modelo Book
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));

        // Adiciona as colunas à tabela
        userTable.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, copiesColumn, yearColumn);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        userTable.setItems(listToDisplay);
    }

    private VBox createSearchContainer(FilteredList<Book> filteredList) {

        VBox searchContainer = new VBox(10);
        searchContainer.setPadding(new Insets(10, 0, 10, 0));

        // --- ID Search ---
        HBox SearchBox = new HBox(10);
        TextField SearchField = new TextField();
        SearchField.setPromptText("Buscar por ID, Título, Author, ISBN ou Ano de Publicação");

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(book -> {
                // Se o campo de busca estiver vazio, mostre todos os usuários.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compara o texto de busca (em minúsculas) com os dados do usuário.
                String lowerCaseFilter = newValue.toLowerCase();
                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo titulo
                } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo autor
                } else if (String.valueOf(book.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo ID
                } else if (String.valueOf(book.getPublicationDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pela data
                } else if (book.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo ISBN
                } else if (String.valueOf(book.getCopiesAvailable()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo numero de copias
                }
                return false; // Não encontrou correspondência
            });
        });

        Button CleanButton = new Button("Limpar");
        CleanButton.setPrefWidth(100);
        CleanButton.setOnAction(event -> SearchField.clear());

        SearchBox.getChildren().addAll(SearchField, CleanButton);
        HBox.setHgrow(SearchField, Priority.ALWAYS);
        searchContainer.getChildren().addAll(SearchBox);

        return searchContainer;
    }

}