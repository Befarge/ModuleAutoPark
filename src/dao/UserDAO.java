package dao;
import entity.User;
import java.sql.*;

public class UserDAO {
    private Connection connection;

    // Конструктор, принимающий объект соединения
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) {
        String query = "INSERT INTO users (login, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
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
                        rs.getString("password")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
