package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.persistence.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UserPlugin implements IPlugin {

    private RegisterPage registerPage;
    private EditPage editPage;
    private ListPage listPage;

    Button registerButton;
    Button editButton;
    Button listButton;

    IUIController uiController;

    public boolean init() {
        System.out.println("======> UserPlugin.init() FOI CHAMADO! <======");

        uiController = ICore.getInstance().getUIController();

        UserManager userManager = new UserManager();

        VBox container = new VBox(8);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-border-width: 1; -fx-border-color: #A0C4FF;");
        Label title = new Label("Usuários");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-background-color: #A0C4FF; -fx-font-size: 20px;");

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

        registerPage = new RegisterPage(userManager);
        editPage = new EditPage(userManager);
        listPage = new ListPage(userManager);

        setupActions();

        uiController.addNavigationButton(container);
        return true;
    }

    private void setupActions() {
        registerButton.setOnAction(event -> {
            // A ação a ser executada é:
            // Chamar o UIController para trocar o conteúdo principal pela nossa tela de
            // usuários.
            uiController.setMainContent(registerPage);
        });

        editButton.setOnAction(event -> {
            uiController.setMainContent(editPage);
        });

        listButton.setOnAction(event -> {
            uiController.setMainContent(listPage);
        });
    }
}
