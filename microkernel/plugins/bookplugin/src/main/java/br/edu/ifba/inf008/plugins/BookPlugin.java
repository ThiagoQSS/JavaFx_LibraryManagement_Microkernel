package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class BookPlugin implements IPlugin
{

    // private RegisterPage registerPage;
    // private EditPage editPage;
    // private ListPage listPage;

    Button registerButton;
    Button editButton;
    Button listButton;

    IUIController uiController;

    public boolean init() {
        System.out.println("======> BookPlugin.init() FOI CHAMADO! <======");

        uiController = ICore.getInstance().getUIController();

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

        registerButton.getStyleClass().add("sidebar-button");

        editButton.setPrefWidth(150);
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setStyle("-fx-border-width: 1;");

        listButton.setPrefWidth(150);
        listButton.setMaxWidth(Double.MAX_VALUE);
        listButton.setStyle("-fx-border-width: 1;");

        container.getChildren().addAll(title, registerButton, editButton, listButton);

        // registerPage = new RegisterPage();
        // editPage = new EditPage();
        // listPage = new ListPage();

        // setupActions();

        uiController.addNavigationButton(container);
        
        return true;
    }
}
