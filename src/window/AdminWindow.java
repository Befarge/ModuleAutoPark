package window;
import db.DatabaseConnection;
import entity.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminWindow extends JFrame {
    private DatabaseConnection db;
    private User user;

    public AdminWindow (DatabaseConnection db, User user) {
        this.db = db;
        this.user = user;

        setTitle("Пункт управления");
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null); // центр экрана
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Кнопка "Личный кабинет"
        JButton changePasswordButton = new JButton("Изменить пароль");
        changePasswordButton.addActionListener(e -> changePassword());
        panel.add(changePasswordButton);

        // Кнопка "Список машин"
        JButton listCarsButton = new JButton("Список машин");
        listCarsButton.addActionListener(e -> clickListCars());
        panel.add(listCarsButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e) {
                db.close();
            }
        });

        add(panel);
    }

    private void changePassword() {
        new ChangePasswordWindow(this, db, user);
    }

    private void clickListCars() {
        new ListCarsAdminWindow(this, db);
    }
}
