import dao.DriverDAO;
import dao.UserDAO;
import db.ConfigReader;
import db.DatabaseConnection;
import window.RegistrationWindow;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connection = db.connect();

        DriverDAO driverDAO = new DriverDAO(connection);
        UserDAO userDAO = new UserDAO(connection);

        // Запускаем GUI в потоке событий Swing
        SwingUtilities.invokeLater(() -> new RegistrationWindow(userDAO, driverDAO));

        db.release();
        connection.close();
    }
}
