package window;
import db.DatabaseConnection;
import entity.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    private DatabaseConnection db;
    private User user;

    public MainWindow(DatabaseConnection db, User user) {
        this.db = db;
        this.user = user;

        setTitle("Главное окно");
        setSize(200, 200);
        setResizable(false);
        setLocationRelativeTo(null); // центр экрана
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUser();
        setVisible(true);
    }

    private void initUser() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Кнопка "Личный кабинет"
        JButton personalCabinetButton = new JButton("Личный кабинет");
        personalCabinetButton.addActionListener(e -> openPersonalCabinet());
        panel.add(personalCabinetButton);

        //Кнопка "Завершить поездку"
        JButton endTheTripButton = new JButton("Завершить поездку");
        endTheTripButton.addActionListener(e -> openEndTheTrip());
        panel.add(endTheTripButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e) {
                db.close();
            }
        });

        add(panel);
    }

    private void openPersonalCabinet() {
        ProfileWindow profileWindow = new ProfileWindow(this, db, user);
    }

    private void openEndTheTrip () {
        boolean isTrip = db.getDriverDAO().getDriverByUserId(user.getId()).isOnTrip();
        System.out.println(isTrip);
        if (isTrip) {
            new CompleteTripWindow(this, db, user);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "У вас нет начатой поездки.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
