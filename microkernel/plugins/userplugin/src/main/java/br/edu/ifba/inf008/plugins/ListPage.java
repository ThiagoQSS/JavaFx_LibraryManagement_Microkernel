package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.plugins.model.User;
import br.edu.ifba.inf008.plugins.persistence.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<User> userList;
    private UserDAO userDAO;

    public ListPage() {
        this.userDAO = new UserDAO(); // inicializando o userDAO
        // 1. Configurações gerais do painel (VBox)
        this.setPadding(new Insets(20));
        this.setSpacing(10);

        // 2. Cria e adiciona um título para a página, cria os inputs no metodo privado
        Label titleLabel = new Label("Lista de Usuários");
        titleLabel.setFont(new Font("Arial", 22));
        titleLabel.setStyle("-fx-padding: 5px");

        VBox searchContainer = createSearchContainer();
        // 3. Configura a Tabela
        setupTable();

        // 4. Adiciona os componentes ao layout
        this.getChildren().addAll(titleLabel, searchContainer, userTable);
    }

    private void setupTable() {
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
        userTable.getColumns().add(idColumn);
        userTable.getColumns().add(nameColumn);
        userTable.getColumns().add(emailColumn);
        userTable.getColumns().add(dateColumn);

        // Ajusta a largura das colunas para preencher o espaço disponível
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Popula a tabela com dados reais dessa vezz
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        // Busca a lista de usuários do banco
        List<User> usersFromDb = userDAO.getAllUsers();
        
        // Converte a List normal para uma ObservableList do JavaFX
        userList = FXCollections.observableArrayList(usersFromDb);
        
        // Define a lista de itens da tabela
        userTable.setItems(userList);
    }
    
    private VBox createSearchContainer() {
        VBox searchContainer = new VBox(10);
        searchContainer.setPadding(new Insets(10, 0, 10, 0));

        // --- ID Search ---
        HBox idSearchBox = new HBox(10);
        TextField idSearchField = new TextField();
        idSearchField.setPromptText("Buscar por ID");
        Button idSearchButton = new Button("Buscar");
        idSearchButton.setPrefWidth(100);
        Button idCleanButton = new Button("Limpar");
        idCleanButton.setPrefWidth(100);
        HBox.setHgrow(idSearchField, Priority.ALWAYS);
        idSearchBox.getChildren().addAll(idSearchField, idSearchButton, idCleanButton);

        // --- Name Search ---
        HBox nameSearchBox = new HBox(10);
        TextField nameSearchField = new TextField();
        nameSearchField.setPromptText("Buscar por Nome");
        Button nameSearchButton = new Button("Buscar");
        nameSearchButton.setPrefWidth(100);
        Button nameCleanButton = new Button("Limpar");
        nameCleanButton.setPrefWidth(100);
        HBox.setHgrow(nameSearchField, Priority.ALWAYS);
        nameSearchBox.getChildren().addAll(nameSearchField, nameSearchButton, nameCleanButton);

        // --- Date Search ---
        HBox dateSearchBox = new HBox(10);
        TextField dateSearchField = new TextField();
        dateSearchField.setPromptText("Buscar por Data (AAAA-MM-DD)");
        Button dateSearchButton = new Button("Buscar");
        dateSearchButton.setPrefWidth(100);
        Button dateCleanButton = new Button("Limpar");
        dateCleanButton.setPrefWidth(100);
        HBox.setHgrow(dateSearchField, Priority.ALWAYS);
        dateSearchBox.getChildren().addAll(dateSearchField, dateSearchButton, dateCleanButton);

        searchContainer.getChildren().addAll(idSearchBox, nameSearchBox, dateSearchBox);

        return searchContainer;
    }


    // No futuro, teremos um método para adicionar usuários a partir de outras
    // páginas
    public void addUser(User user) {
        userList.add(user);
    }
}