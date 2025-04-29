package dao;
import customException.*;
import entity.User;
import types.UserRole;
import types.UserStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    // Конструктор, принимающий объект соединения
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public Integer addUser(User user) throws SQLException {
        String query = "INSERT INTO users (login, password, role, status) VALUES (?, ?, ?::user_role, ?::user_status)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());
            stmt.setString(4, user.getStatus().toString());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next())
                return keys.getInt("user_id");
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getSQLState().equals("23505")) {
                String msg = ex.getMessage();
                ex.printStackTrace();
                if (msg.contains("un_login"))
                    throw new UniquenessLoginException("Такой логин уже существует.");
                else
                    throw new UniquenessException("Ошибка уникальности");
            } else if (ex.getSQLState().equals("23502")) {
                throw new NullException("Не все данные введены");
            } else {
                throw ex;
            }
        }
        return null;
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
                        UserRole.valueOf(rs.getString("role")),
                        UserStatus.valueOf(rs.getString("status"))
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

    public User getUserByLogin (String login) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        login,
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role")),
                        UserStatus.valueOf(rs.getString("status"))
                );
            } else {
                return null; // пользователь не найден
            }
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET login = ?, password = ?, role = ?::user_role, status = ?::user_status WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());
            stmt.setString(4, user.getStatus().toString());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getSQLState().equals("23502")) {
                throw new NullException("Не все данные введены");
            } else {
                throw ex;
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role")),
                        UserStatus.valueOf(rs.getString("status"))
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
