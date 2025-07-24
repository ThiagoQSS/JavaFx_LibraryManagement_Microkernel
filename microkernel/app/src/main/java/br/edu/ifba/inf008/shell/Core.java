package br.edu.ifba.inf008.shell;

import java.sql.Connection;
import java.sql.SQLException;

import br.edu.ifba.inf008.interfaces.*;
import br.edu.ifba.inf008.shell.persistence.DatabaseConnection;
import javafx.application.Application;
import javafx.application.Platform;

public class Core extends ICore {

    private IAuthenticationController authenticationController = new AuthenticationController();
    private IIOController ioController = new IOController();
    private IPluginController pluginController = new PluginController();

    private Core() {
    }

    public static boolean init() {
        if (instance != null) {
            System.out.println("Fatal error: core is already initialized!");
            System.exit(-1);
        }

        instance = new Core();
        System.out.println("Lauching the UIController!");
        UIController.launch(UIController.class);

        return true;
    }

    public IUIController getUIController() {
        return UIController.getInstance();
    }

    public IAuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public IIOController getIOController() {
        return ioController;
    }

    public IPluginController getPluginController() {
        return pluginController;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Delega a responsabilidade para a nossa classe de conexão centralizada
        return DatabaseConnection.getConnection();
    }
}
