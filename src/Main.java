import db.ConfigReader;
import db.DatabaseConnection;
import window.AdminWindow;
import window.user.LoginWindow;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);

        // Запускаем GUI в потоке событий Swing
        SwingUtilities.invokeLater(() -> {
//            try {
//                new AdminWindow(db, db.getUserDAO().getUserByLogin("admin"));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
            new LoginWindow(db);
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
