package br.edu.ifba.inf008.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ICore
{
    public static ICore getInstance() {
        return instance;
    }

    public abstract IUIController getUIController();
    public abstract IAuthenticationController getAuthenticationController();
    public abstract IIOController getIOController();
    public abstract IPluginController getPluginController();
    public abstract Connection getConnection() throws SQLException;


    protected static ICore instance = null;
}
