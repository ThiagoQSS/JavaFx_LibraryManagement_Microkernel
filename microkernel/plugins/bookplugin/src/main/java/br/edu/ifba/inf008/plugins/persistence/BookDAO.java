package br.edu.ifba.inf008.plugins.persistence;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.plugins.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {

    /**
     * Busca todos os books cadastrados no banco de dados.
     * 
     * @return Uma lista de objetos Books.
     */
    public List<Book> getAllBooks() {
        // A lista que irá armazenar os books encontrados
        List<Book> books = new ArrayList<>();

        // O comando SQL para selecionar todos os books.
        String sql = "SELECT book_id, title, author, isbn, published_year, copies_available FROM books";

        // Usamos um 'try-with-resources' para garantir que a conexão e o statement
        // sejam fechados automaticamente, mesmo que ocorra um erro.
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            // Itera sobre cada linha retornada pelo banco de dados
            while (rs.next()) {
                // Extrai os dados de cada coluna da linha atual
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                int published_year = rs.getInt("published_year");
                int copies_available = rs.getInt("copies_available");
                // Cria um objeto User com os dados e o adiciona à lista
                books.add(new Book(id, title, author, isbn, copies_available, published_year));
            }
        } catch (SQLException e) {
            // Imprime o erro no console em caso de falha
            System.err.println("Erro ao buscar livros: " + e.getMessage());
            // Opcional: Você pode querer lançar uma exceção customizada aqui.
        }

        return books;
    }

    /**
     * Salva um novo book no banco de dados.
     * 
     * @param user O objeto Book a ser salvo (o ID pode ser qualquer valor, será
     *             ignorado).
     * @return O ID gerado pelo banco de dados para o novo book.
     * @throws SQLException se ocorrer um erro durante a inserção.
     */
    public int saveBook(Book book) throws SQLException {
        // O comando SQL INSERT com placeholders (?) para os valores.
        String sql = "INSERT INTO books (title, author, isbn, published_year, copies_available) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        // Usamos try-with-resources para garantir que a conexão e o statement sejam
        // fechados.
        // Criamos o PreparedStatement, pedindo para retornar as chaves geradas (o ID).
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            // 1. Substitui os placeholders (?) pelos dados do objeto Book.
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            // Para datas, setObject é uma forma robusta de passar o tipo correto.
            pstmt.setObject(4, book.getPublicationDate());
            pstmt.setInt(5, book.getCopiesAvailable());

            // 2. Executa o comando de inserção.
            int affectedRows = pstmt.executeUpdate();

            // 3. Verifica se a inserção funcionou.
            if (affectedRows > 0) {
                // 4. Recupera o ID gerado pelo banco.
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar livro: " + e.getMessage());
            // Relança a exceção para que a camada que chamou o método saiba do erro.
            throw e;
        }

        return generatedId;
    }

    public Optional<Book> getBookById(int bookId) throws SQLException {
        String sql = "SELECT title, author, isbn, published_year, copies_available FROM books WHERE book_id = ?";

        // Usamos try-with-resources para garantir que a conexão e o statement sejam
        // fechados.
        // Criamos o PreparedStatement, pedindo para retornar as chaves geradas (o ID).
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Usando if pq só devemos receber um resultado por ID
                if (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String isbn = rs.getString("isbn");
                    int publishedDate = rs.getInt("published_year");
                    int copiesAvailable = rs.getInt("copies_available");
                    Book book = new Book(bookId, title, author, isbn, copiesAvailable, publishedDate);
                    return Optional.of(book);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao resgatar usuário: " + e.getMessage());
        }
        // Se o book não foi encontrado ou se ocorreu um erro, retorna um Optional
        // vazio.
        return Optional.empty();
    }

    public void updateBook(int id, String title, String author, String isbn, int publishedDate, int copiesAvailable) throws SQLException {
        String sql = "UPDATE users SET title = ?, author = ?, isbn = ?, published_year = ?, copies_available = ? WHERE user_id = ?";

        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);
            pstmt.setObject(4, publishedDate);
            pstmt.setInt(5, copiesAvailable);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}