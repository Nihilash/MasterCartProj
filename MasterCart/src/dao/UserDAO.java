package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDAO {

    // Fetch all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, mobile, password_hash FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("mobile"),
                    rs.getString("password_hash")
                );
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Insert new user
    public void addUser(User user) {
        String sql = "INSERT INTO users (username, mobile, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getMobile());
            ps.setString(3, user.getPasswordHash());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find user by username (returns null if not exists)
    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, mobile, password_hash FROM users WHERE username = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("mobile"),
                        rs.getString("password_hash")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check existence of username
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
