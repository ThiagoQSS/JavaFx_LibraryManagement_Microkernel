package br.edu.ifba.inf008.plugins.persistence;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.plugins.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    /**
     * Busca todos os usuários cadastrados no banco de dados.
     * 
     * @return Uma lista de objetos User.
     */
    public List<User> getAllUsers() {
        // A lista que irá armazenar os usuários encontrados
        List<User> users = new ArrayList<>();

        // O comando SQL para selecionar todos os usuários.
        // Mudar isso de acordo com a tabela SQL
        String sql = "SELECT user_id, name, email, registered_at FROM users";

        // Usamos um 'try-with-resources' para garantir que a conexão e o statement
        // sejam fechados automaticamente, mesmo que ocorra um erro.
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            // Itera sobre cada linha retornada pelo banco de dados
            while (rs.next()) {
                // Extrai os dados de cada coluna da linha atual
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                LocalDate registrationDate = rs.getTimestamp("registered_at").toLocalDateTime().toLocalDate();

                // Cria um objeto User com os dados e o adiciona à lista
                users.add(new User(id, name, email, registrationDate));
            }
        } catch (SQLException e) {
            // Imprime o erro no console em caso de falha
            System.err.println("Erro ao buscar usuários: " + e.getMessage());
            // Opcional: Você pode querer lançar uma exceção customizada aqui.
        }

        return users;
    }

    /**
     * Salva um novo usuário no banco de dados.
     * 
     * @param user O objeto User a ser salvo (o ID pode ser qualquer valor, será
     *             ignorado).
     * @return O ID gerado pelo banco de dados para o novo usuário.
     * @throws SQLException se ocorrer um erro durante a inserção.
     */
    public int saveUser(User user) throws SQLException {
        // O comando SQL INSERT com placeholders (?) para os valores.
        String sql = "INSERT INTO users (name, email, registered_at) VALUES (?, ?, ?)";
        int generatedId = -1;

        // Usamos try-with-resources para garantir que a conexão e o statement sejam
        // fechados.
        // Criamos o PreparedStatement, pedindo para retornar as chaves geradas (o ID).
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            // 1. Substitui os placeholders (?) pelos dados do objeto User.
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            // Para datas, setObject é uma forma robusta de passar o tipo correto.
            pstmt.setObject(3, user.getRegistrationDate());

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
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
            // Relança a exceção para que a camada que chamou o método saiba do erro.
            throw e;
        }

        return generatedId;
    }

    public Optional<User> getUserById(int userId) throws SQLException {
        String sql = "SELECT name, email, registered_at FROM users WHERE user_id = ?";

        // Usamos try-with-resources para garantir que a conexão e o statement sejam
        // fechados.
        // Criamos o PreparedStatement, pedindo para retornar as chaves geradas (o ID).
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Usando if pq só devemos receber um resultado por ID
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    LocalDate registrationDate = rs.getTimestamp("registered_at").toLocalDateTime().toLocalDate();
                    User user = new User(userId, name, email, registrationDate);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao resgatar usuário: " + e.getMessage());
        }
        // Se o usuário não foi encontrado ou se ocorreu um erro, retorna um Optional
        // vazio.
        return Optional.empty();
    }

    public void updateUser(int id, String name, String email) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";

        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = ICore.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}