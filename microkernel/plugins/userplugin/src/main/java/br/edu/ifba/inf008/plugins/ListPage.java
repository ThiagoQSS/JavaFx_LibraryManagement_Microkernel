package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.plugins.model.User;
import br.edu.ifba.inf008.plugins.persistence.UserDAO;
import br.edu.ifba.inf008.plugins.persistence.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Representa a página que exibe uma lista de todos os usuários cadastrados.
 */
public class ListPage extends VBox {

    private TableView<User> userTable;

    public ListPage(UserManager userManager) {
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20));
        this.setSpacing(10);

        // 2. Cria e adiciona um título para a página, cria os inputs no metodo privado
        Label titleLabel = new Label("Lista de Usuários");
        titleLabel.setFont(new Font("Arial", 22));
        titleLabel.setStyle("-fx-padding: 5px");

        ObservableList<User> masterUserList = userManager.getUserList();
        FilteredList<User> filteredUserList = new FilteredList<>(masterUserList, p -> true);

        VBox searchContainer = createSearchContainer(filteredUserList);
        // 3. Configura a Tabela
        setupTable(filteredUserList);

        // 4. Adiciona os componentes ao layout
        this.getChildren().addAll(titleLabel, searchContainer, userTable);
    }

    private void setupTable(FilteredList<User> listToDisplay) {
        // Cria a Tabela
        userTable = new TableView<>();

        // Cria as Colunas
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<User, String> nameColumn = new TableColumn<>("Nome");
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        TableColumn<User, LocalDate> dateColumn = new TableColumn<>("Data de Cadastro");

        // Conecta as colunas com as propriedades do modelo User
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        // Adiciona as colunas à tabela
        userTable.getColumns().addAll(idColumn, nameColumn, emailColumn, dateColumn);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        userTable.setItems(listToDisplay);
    }

    private VBox createSearchContainer(FilteredList<User> filteredList) {

        VBox searchContainer = new VBox(10);
        searchContainer.setPadding(new Insets(10, 0, 10, 0));

        // --- ID Search ---
        HBox SearchBox = new HBox(10);
        TextField SearchField = new TextField();
        SearchField.setPromptText("Buscar por ID, Nome, Email ou Data (AAAA-MM-DD)");

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {
                // Se o campo de busca estiver vazio, mostre todos os usuários.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compara o texto de busca (em minúsculas) com os dados do usuário.
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo nome
                } else if (user.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo email
                } else if (String.valueOf(user.getId()).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo ID
                } else if (user.getRegistrationDate().toString().contains(lowerCaseFilter)) {
                    return true; // Filtra pela data
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