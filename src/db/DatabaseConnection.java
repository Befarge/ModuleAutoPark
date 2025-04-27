package db;
import dao.CarDAO;
import dao.DriverDAO;
import dao.TripDAO;
import dao.UserDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;
    private UserDAO userDAO;
    private DriverDAO driverDAO;
    private TripDAO tripDAO;
    private CarDAO carDAO;

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
            this.tripDAO = new TripDAO(connection);
            this.carDAO = new CarDAO(connection);
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }


    public UserDAO getUserDAO() {
        return userDAO;
    }

    public TripDAO getTripDAO() {
        return tripDAO;
    }

    public CarDAO getCarDAO() {
        return carDAO;
    }

    public DriverDAO getDriverDAO() {
        return driverDAO;
    }

    public Connection getConnection() { return connection; };

    public void commit () throws SQLException {
        connection.setAutoCommit(true);
        connection.setAutoCommit(false);
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

