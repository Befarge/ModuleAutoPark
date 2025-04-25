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
        setSize(600, 400);
        setLocationRelativeTo(null); // центр экрана
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUser();
        setupListeners();
        setVisible(true);
    }

    private void initUser() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Добавим панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Кнопка "Личный кабинет"
        JButton personalCabinetButton = new JButton("Личный кабинет");
        personalCabinetButton.addActionListener(e -> openPersonalCabinet());

        buttonPanel.add(personalCabinetButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel);
    }

    private void openPersonalCabinet() {
        ProfileWindow profileWindow = new ProfileWindow(this, db, user);
        profileWindow.setVisible(true);
    }

    private void setupListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                db.close();
            }
        });
    }
}
