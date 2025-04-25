import customException.NullException;
import db.ConfigReader;
import db.DatabaseConnection;
import window.LoginWindow;
import window.MainWindow;
import window.RegistrationWindow;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);

        // Запускаем GUI в потоке событий Swing
        SwingUtilities.invokeLater(() -> {
//                new MainWindow(
//                        db,
//                        db.getUserDAO().getUserByLogin("befarge")
//                );
            new LoginWindow(db);
        });
    }
}
