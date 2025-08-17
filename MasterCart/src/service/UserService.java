package service;

import dao.UserDAO;
import model.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Login - verifies username and password (plain text for now).
     * Returns the User object if valid, otherwise null.
     */
    public User login(String username, String passwordPlain) {
        User u = userDAO.getUserByUsername(username);
        if (u != null && u.getPasswordHash().equals(passwordPlain)) {
            return u;
        }
        return null;
    }

    /**
     * Register user. Returns true if registration succeeded.
     * If username already exists returns false.
     */
    public boolean register(User newUser) {
        if (newUser == null || newUser.getUsername() == null) return false;
        if (userDAO.existsByUsername(newUser.getUsername())) {
            return false;
        }
        userDAO.addUser(newUser);
        return true;
    }
}
