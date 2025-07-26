package br.edu.ifba.inf008.plugins.persistence;

import br.edu.ifba.inf008.plugins.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Classe central que gerencia os dados de usuários,
 * servindo como única fonte da verdade para a UI.
 */
public class BookManager {

    private final ObservableList<Book> bookList;
    private final BookDAO bookDAO;

    public BookManager() {
        this.bookDAO = new BookDAO();
        // Inicializa a lista como uma ObservableList vazia.
        this.bookList = FXCollections.observableArrayList();
        // Carrega os dados iniciais do banco.
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        List<Book> booksFromDb = this.bookDAO.getAllBooks();
        this.bookList.setAll(booksFromDb); // Usa setAll para substituir o conteúdo da lista
    }

    /**
     * Retorna a lista observável de usuários. A UI se vinculará a esta lista.
     * 
     * @return A ObservableList de usuários.
     */
    public ObservableList<Book> getBookList() {
        return this.bookList;
    }

    /**
     * Salva um novo usuário, persistindo no banco e atualizando a lista em memória.
     * 
     * @param name  O nome do novo usuário.
     * @param email O email do novo usuário.
     */
    public int saveNewBook(String title, String author, String isbn, int publishedDate, int copiesAvailable) throws SQLException {
        // Cria o objeto User. O -1 no ID é um placeholder.
        Book newBook = new Book(-1, title, author, isbn, copiesAvailable, publishedDate);

        // Salva no banco de dados e obtém o ID real gerado.
        int generatedId = bookDAO.saveBook(newBook);

        // Cria um objeto final com o ID correto e o adiciona à lista observável.
        Book persistedBook = new Book(generatedId, title, author, isbn, copiesAvailable, publishedDate);
        this.bookList.add(persistedBook);

        return generatedId;
    }

    public Optional<Book> getUserById(int id) throws SQLException {
        return this.bookDAO.getBookById(id);
    }

    public void updateBook(int id, String title, String author, String isbn, int publishedDate, int copiesAvailable) throws SQLException {
        this.bookDAO.updateBook(id, title, author, isbn, publishedDate, copiesAvailable);
        for (Book book : bookList) {
            if (book.getId() == id) {
                book.setTitle(title);
                book.setAuthor(author);
                book.setIsbn(isbn);
                book.setCopiesAvailable(copiesAvailable);
                book.setPublicationDate(publishedDate);
                break;
            }
        }
    }

    public void deleteBook(int id) throws SQLException {
        this.bookDAO.deleteBook(id);
        this.bookList.removeIf(book -> book.getId() == id);
    }
}