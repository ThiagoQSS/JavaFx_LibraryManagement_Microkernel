package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.model.Book;
import br.edu.ifba.inf008.plugins.persistence.BookManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class BookPlugin implements IPlugin
{

    private RegisterPage registerPage;
    private EditPage editPage;
    private ListPage listPage;

    private Button registerButton;
    private Button editButton;
    private Button listButton;

    IUIController uiController;

    public boolean init() {
        System.out.println("======> BookPlugin.init() FOI CHAMADO! <======");

        uiController = ICore.getInstance().getUIController();

        BookManager bookManager = new BookManager();

        VBox container = new VBox(8);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-border-width: 1; -fx-border-color: #AACCAA;");
        Label title = new Label("Livros");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-background-color: #AACCAA; -fx-font-size: 20px;");

        registerButton = new Button("Cadastrar");
        editButton = new Button("Editar");
        listButton = new Button("Listar");

        registerButton.setPrefWidth(150);
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setStyle("-fx-border-width: 1;");

        editButton.setPrefWidth(150);
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setStyle("-fx-border-width: 1;");

        listButton.setPrefWidth(150);
        listButton.setMaxWidth(Double.MAX_VALUE);
        listButton.setStyle("-fx-border-width: 1;");

        container.getChildren().addAll(title, registerButton, editButton, listButton);

        registerPage = new RegisterPage(bookManager);
        editPage = new EditPage(bookManager);
        listPage = new ListPage(bookManager);

        setupActions();

        uiController.addNavigationButton(container);
        
        return true;
    }

    private void setupActions() {
        registerButton.setOnAction(e -> uiController.setMainContent(registerPage));
        editButton.setOnAction(e -> uiController.setMainContent(editPage));
        listButton.setOnAction(e -> uiController.setMainContent(listPage));
    }
}
