package window.user;
import customException.UnsuccessfulValidationException;
import db.DatabaseConnection;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import types.UserRole;
import types.UserStatus;
import window.AdminWindow;
import window.MainWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    private JTextField loginField;
    private JPasswordField passwordField;
    private final DatabaseConnection db;

    public LoginWindow(DatabaseConnection db) {
        this.db = db;

        setTitle("Авторизация");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null); // Центр экрана

        initComponents();
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

        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(e -> logIn());

        JButton registerButton = new JButton("Зарегистрироваться");
        registerButton.addActionListener(e -> callRegistrationWindow());

        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
    }

    public void logIn() {
        String login = StringUtils.trimToNull(loginField.getText());
        String password = StringUtils.trimToNull(new String(passwordField.getPassword()));

        if (login == null || password == null) {
            JOptionPane.showMessageDialog(LoginWindow.this,
                    "Введите логин и пароль",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            User user = db.getUserDAO().getUserByLogin(login);

            if (user != null) {
                if (user.getPassword().equals(password)) {
                    if (user.getStatus() == UserStatus.BLOCKED) {
                        JOptionPane.showMessageDialog(LoginWindow.this,
                                "Ваш аккаунт заблокирован",
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else if (user.getStatus() == UserStatus.WAIT) {
                        JOptionPane.showMessageDialog(LoginWindow.this,
                                "Ваш аккаунт не подтвержден",
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                LoginWindow.this,
                                "Успешный вход! Добро пожаловать"
                        );

                        switch (user.getRole()) {
                            case USER -> {
                                new MainWindow(db, user);
                            } case ADMIN -> {
                                new AdminWindow(db, user);
                            } case MANAGER -> {
                                JOptionPane.showMessageDialog(
                                        LoginWindow.this,
                                        "Данная система пока в реализации"
                                );
                            }
                        }
                        dispose();
                    }
                } else {
                    throw new UnsuccessfulValidationException("Неверный пароль");
                }
            } else {
                throw new UnsuccessfulValidationException("Такого логина не существует");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(LoginWindow.this,
                    "Неизвестная ошибка с БД",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (UnsuccessfulValidationException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(LoginWindow.this,
                    "Неверный логин или пароль",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void callRegistrationWindow (){
        new RegistrationWindow(db, this);
    }
}


