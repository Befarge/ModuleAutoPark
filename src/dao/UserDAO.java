package dao;
import entity.User;
import types.UserRole;

import java.sql.*;

public class UserDAO {
    private Connection connection;

    // Конструктор, принимающий объект соединения
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) {
        String query = "INSERT INTO users (login, password, role) VALUES (?, ?, ?::user_role)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Пользователь не был найден");
            } else {
                System.out.println("Было удалено пользователей " + affectedRows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                return new User(
                        rs.getInt("user_id"),
                        login,
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );
            } else {
                return null; // пользователь не найден
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
