package br.edu.ifba.inf008.plugins.persistence;

import br.edu.ifba.inf008.plugins.model.User;
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
public class UserManager {

    private final ObservableList<User> userList;
    private final UserDAO userDAO;

    public UserManager() {
        this.userDAO = new UserDAO();
        // Inicializa a lista como uma ObservableList vazia.
        this.userList = FXCollections.observableArrayList();
        // Carrega os dados iniciais do banco.
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        List<User> usersFromDb = this.userDAO.getAllUsers();
        this.userList.setAll(usersFromDb); // Usa setAll para substituir o conteúdo da lista
    }

    /**
     * Retorna a lista observável de usuários. A UI se vinculará a esta lista.
     * 
     * @return A ObservableList de usuários.
     */
    public ObservableList<User> getUserList() {
        return this.userList;
    }

    /**
     * Salva um novo usuário, persistindo no banco e atualizando a lista em memória.
     * 
     * @param name  O nome do novo usuário.
     * @param email O email do novo usuário.
     */
    public int saveNewUser(String name, String email) throws SQLException {
        // Cria o objeto User. O -1 no ID é um placeholder.
        User newUser = new User(-1, name, email, LocalDate.now());

        // Salva no banco de dados e obtém o ID real gerado.
        int generatedId = userDAO.saveUser(newUser);

        // Cria um objeto final com o ID correto e o adiciona à lista observável.
        User persistedUser = new User(generatedId, name, email, newUser.getRegistrationDate());
        this.userList.add(persistedUser);

        return generatedId;
    }

    public Optional<User> getUserById(int id) throws SQLException {
        return this.userDAO.getUserById(id);
    }

    public void updateUser(int id, String name, String email) throws SQLException {
        this.userDAO.updateUser(id, name, email);
        for (User user : userList) {
            if (user.getId() == id) {
                user.setName(name);
                user.setEmail(email);
                break;
            }
        }
    }

    public void deleteUser(int id) throws SQLException {
        this.userDAO.deleteUser(id);
        this.userList.removeIf(user -> user.getId() == id);
    }
}