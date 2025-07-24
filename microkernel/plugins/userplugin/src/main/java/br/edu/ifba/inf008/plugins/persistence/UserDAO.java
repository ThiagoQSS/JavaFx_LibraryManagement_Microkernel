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

    // No futuro, outros métodos como saveUser(User user), updateUser(User user),
    // etc. virão aqui.
}