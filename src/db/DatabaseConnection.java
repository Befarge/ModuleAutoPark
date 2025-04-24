package db;
import dao.DriverDAO;
import dao.UserDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;
    private UserDAO userDAO;
    private DriverDAO driverDAO;

    public DatabaseConnection(ConfigReader configReader) {
        try {
            Connection connection = DriverManager.getConnection(
                    configReader.getUrl(),
                    configReader.getUsername(),
                    configReader.getPassword()
            );

            this.connection = connection;
            this.userDAO = new UserDAO(connection);
            this.driverDAO = new DriverDAO(connection);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }


    public UserDAO getUserDAO() {
        return userDAO;
    }

    public DriverDAO getDriverDAO() {
        return driverDAO;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с БД закрыто.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

