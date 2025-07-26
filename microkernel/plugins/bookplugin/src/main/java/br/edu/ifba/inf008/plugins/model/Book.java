package br.edu.ifba.inf008.plugins.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Usando *Property para o TableView atualizar automaticamente

public class Book {

    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;
    private final IntegerProperty copiesAvailable;
    private final IntegerProperty publicationDate;

    public Book(
            int id, 
            String title, 
            String author, 
            String isbn, 
            int copiesAvailable,
            int publicationDate
        ) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.copiesAvailable = new SimpleIntegerProperty(copiesAvailable);
        this.publicationDate = new SimpleIntegerProperty(publicationDate);
    }

    // --- Getters para os valores ---
    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getIsbn() { return isbn.get(); }
    public int getCopiesAvailable() { return copiesAvailable.get(); }
    public int getPublicationDate() { return publicationDate.get(); }

    // --- Getters para as Propriedades (necessário para o TableView) ---
    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty isbnProperty() { return isbn; }
    public IntegerProperty copiesAvailableProperty() { return copiesAvailable; }
    public IntegerProperty publicationDateProperty() { return publicationDate; }

    // --- Setters para os valores ---
    public void setTitle(String title) {this.title.set(title);}
    public void setAuthor(String author) {this.author.set(author);}
    public void setIsbn(String isbn) {this.isbn.set(isbn);}
    public void setCopiesAvailable(int copiesAvailable) {this.copiesAvailable.set(copiesAvailable);}
    public void setPublicationDate(int publicationDate) {this.publicationDate.set(publicationDate);}
}