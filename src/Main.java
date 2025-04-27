import db.ConfigReader;
import db.DatabaseConnection;
import window.MainWindow;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);

        // Запускаем GUI в потоке событий Swing
        SwingUtilities.invokeLater(() -> {
            try {
                new MainWindow(
                        db,
                        db.getUserDAO().getUserByLogin("venikov")
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
