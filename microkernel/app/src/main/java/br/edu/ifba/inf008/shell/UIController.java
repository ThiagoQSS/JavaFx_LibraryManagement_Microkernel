package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.shell.PluginController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;

public class UIController extends Application implements IUIController
{
    private ICore core;
    private static UIController uiController;
    private MenuBar menuBar;
    private TabPane tabPane;
    // Novas variáveis para controlar os painéis
    private VBox navigationPane;
    private StackPane contentPane;

    public UIController() {
    }

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gerenciamento de Livraria - Thiago Quadros");

        // Container raiz
        BorderPane root = new BorderPane();
        //Menu bar no topo
        this.menuBar = new MenuBar();
        root.setTop(menuBar);
        // Adiciona um menu "Arquivo > Sair" para funcionalidade básica
        MenuItem exitItem = createMenuItem("Arquivo", "Sair");
        exitItem.setOnAction(e -> Platform.exit());

        // 2. Criar o SplitPane que dividirá a tela
        SplitPane splitPane = new SplitPane();

        // 3. Painel da Esquerda (Navegação)
        // Usamos um VBox para empilhar os botões que os plugins criarão
        this.navigationPane = new VBox(8); // 8px de espaçamento entre botões
        this.navigationPane.setPadding(new Insets(10)); // Margem interna de 10px
        this.navigationPane.setStyle("-fx-background-color: #ECECEC;"); // Um fundo para diferenciar

        // 4. Painel da Direita (Conteúdo)
        // Usamos um StackPane para que as telas dos plugins possam ser trocadas facilmente
        this.contentPane = new StackPane();
        this.contentPane.setPadding(new Insets(10));

        // Mensagem inicial
        Label welcomeLabel = new Label("Selecione um módulo no menu à esquerda.");
        this.contentPane.getChildren().add(welcomeLabel);

        //Add os painéis esquerdo e direito ao SplitPane
        splitPane.getItems().addAll(this.navigationPane, this.contentPane);
        // Define a coluna da esquerda com 20% da largura
        splitPane.setDividerPositions(0.20); 

        // Coloca o SplitPane no centro do BorderPane
        root.setCenter(splitPane);
   
        Scene scene = new Scene(root, 1280, 720);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // 7. Inicia o carregamento dos plugins
        // No futuro, os plugins chamarão os métodos abaixo para preencher a UI
        Core.getInstance().getPluginController().init();
        // VBox vBox = new VBox(menuBar);

        // tabPane = new TabPane();
        // tabPane.setSide(Side.BOTTOM);

        // vBox.getChildren().addAll(tabPane);
    }

    public MenuItem createMenuItem(String menuText, String menuItemText) {
        // Criar o menu caso ele nao exista
        Menu newMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuText)) {
                newMenu = menu;
                break;
            }
        }
        if (newMenu == null) {
            newMenu = new Menu(menuText);
            menuBar.getMenus().add(newMenu);
        }

        // Criar o menu item neste menu
        MenuItem menuItem = new MenuItem(menuItemText);
        newMenu.getItems().add(menuItem);

        return menuItem;
    }

    /**
     * Permite que um plugin adicione um botão ao painel de navegação esquerdo.
     * @param button O botão a ser adicionado.
     */
    public void addNavigationButton(VBox vbox) {
        this.navigationPane.getChildren().add(vbox);
    }

    /**
     * Permite que um plugin defina a sua interface gráfica na área de conteúdo principal.
     * @param content O Node (layout) da interface do plugin.
     */
    public void setMainContent(Node content) {
        this.contentPane.getChildren().clear(); // Limpa o conteúdo anterior
        this.contentPane.getChildren().add(content); // Adiciona o novo
    }
}
