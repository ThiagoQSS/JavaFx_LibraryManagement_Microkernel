package br.edu.ifba.inf008.plugins.model;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Usando *Property para o TableView atualizar automaticamente

public class User {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final ObjectProperty<LocalDate> registrationDate;

    public User(int id, String name, String email, LocalDate registrationDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.registrationDate = new SimpleObjectProperty<>(registrationDate);
    }

    // --- Getters para os valores ---
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public LocalDate getRegistrationDate() { return registrationDate.get(); }

    // --- Getters para as Propriedades (necessário para o TableView) ---
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> registrationDateProperty() { return registrationDate; }

    // --- Setters para os valores ---
    public void setName(String name) {this.name.set(name);}
    public void setEmail(String email) {this.email.set(email);}
}