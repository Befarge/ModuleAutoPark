import db.ConfigReader;
import db.DatabaseConnection;
import entity.Trip;
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

//        db.getTripDAO().addTrip(new Trip(
//                4,
//                1
//        ));
//
//        db.commit();

//        try {
//            db.getConnection().rollback();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        String original = db.getTripDAO().getTripById(10).getStartTime();
//        System.out.println(original.substring(0, 16));  // Берём только до минут

//        db.getTripDAO().finishTrip(100, 20, 10);
//        db.commit();
    }
}
