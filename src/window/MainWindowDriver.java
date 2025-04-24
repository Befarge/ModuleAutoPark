package window;
import db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindowDriver extends JFrame {
    private DatabaseConnection db;

    public MainWindowDriver(DatabaseConnection db) {
        this.db = db;
        setTitle("Главное окно");
        setSize(600, 400);
        setLocationRelativeTo(null); // центр экрана
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setupListeners();
        setVisible(true);
    }

    private void initUI() {
        JLabel label = new JLabel("Добро пожаловать в главное окно!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        add(label, BorderLayout.CENTER);
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
