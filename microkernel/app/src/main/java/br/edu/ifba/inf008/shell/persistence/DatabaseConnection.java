package br.edu.ifba.inf008.shell.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária responsável por gerenciar e fornecer
 * a conexão com o banco de dados MariaDB.
 */
public class DatabaseConnection {

    // --- DADOS DA CONEXÃO ---
    // ATENÇÃO: Verifique se estes dados correspondem aos do seu ambiente Docker.
    // O nome do banco de dados ('library_db') é um exemplo.
    private static final String DATABASE_URL = "jdbc:mariadb://localhost:3307/bookstore";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "root"; 
    // Coloque a senha definida no seu docker

    /**
     * Tenta estabelecer e retornar uma nova conexão com o banco de dados.
     * O método que chamar esta função é responsável por fechar a conexão.
     *
     * @return um objeto Connection com a conexão estabelecida.
     * @throws SQLException se ocorrer um erro ao tentar conectar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Opcional: Carrega a classe do driver. Em versões modernas do JDBC, isso é automático.
            // Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Falha na conexão com o banco de dados: " + e.getMessage());
            throw e;
        }
    }
}