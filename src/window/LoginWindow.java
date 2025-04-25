package window;
import db.DatabaseConnection;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private DatabaseConnection db;

    public LoginWindow(DatabaseConnection db) {
        this.db = db;
        setTitle("Авторизация");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Центр экрана

        initComponents();
        setupListeners();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Логин:"));
        loginField = new JTextField();
        panel.add(loginField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Войти");
        registerButton = new JButton("Зарегистрироваться");
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
    }

    private void setupListeners() {
        loginButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = StringUtils.trimToNull(loginField.getText());
                String password = StringUtils.trimToNull(new String(passwordField.getPassword()));

                try {
                    if (login == null || password == null) {
                        JOptionPane.showMessageDialog(LoginWindow.this,
                                "Введите логин и пароль",
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        User user = db.getUserDAO().getUserByLogin(login);

                        if (user != null) {
                            if (user.getPassword().equals(password)) {
                                JOptionPane.showMessageDialog(
                                        LoginWindow.this,
                                        "Успешный вход! Добро пожаловать"
                                );

                                new MainWindow(db, user);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(
                                        LoginWindow.this,
                                        "Неверный логин или пароль"
                                );
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    LoginWindow.this,
                                    "Неверный логин или пароль"
                            );
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        registerButton.addActionListener(e -> {
            setVisible(false); // скрываем окно авторизации
            RegistrationWindow regWindow = new RegistrationWindow(db, this);
            regWindow.setVisible(true);
        });


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                db.close();
            }
        });
    }
}


