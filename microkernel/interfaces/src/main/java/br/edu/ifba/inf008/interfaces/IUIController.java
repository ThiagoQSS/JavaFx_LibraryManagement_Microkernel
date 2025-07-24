package br.edu.ifba.inf008.interfaces;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public interface IUIController
{
    public abstract MenuItem createMenuItem(String menuText, String menuItemText);
    public abstract void addNavigationButton(VBox vBox);
    public abstract void setMainContent(Node content);
}
