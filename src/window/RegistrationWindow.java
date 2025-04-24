package window;
import db.DatabaseConnection;
import entity.Driver;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import types.UserRole;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class RegistrationWindow extends JFrame {
    private DatabaseConnection db;

    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationWindow(DatabaseConnection db) {
        this.db = db;

        setTitle("Регистрация");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setupListeners();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        // Метки и поля
        panel.add(new JLabel("Имя:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Отчество:"));
        middleNameField = new JTextField();
        panel.add(middleNameField);

        panel.add(new JLabel("Фамилия:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Возраст:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Телефон:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Логин:"));
        loginField = new JTextField();
        panel.add(loginField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        registerButton = new JButton("Зарегистрироваться");
        panel.add(new JLabel()); // пустая ячейка
        panel.add(registerButton);

        add(panel);
    }

    private void setupListeners() {
        registerButton.addActionListener(e -> {
            String firstName = StringUtils.trimToNull(firstNameField.getText());
            String middleName = StringUtils.trimToNull(middleNameField.getText());
            String lastName = StringUtils.trimToNull(lastNameField.getText());
            String ageText = StringUtils.trimToNull(ageField.getText());
            String phone = StringUtils.trimToNull(phoneField.getText());
            String login = StringUtils.trimToNull(loginField.getText());
            String password = StringUtils.trimToNull(new String(passwordField.getPassword()));

            try {
                db.getConnection().setAutoCommit(false); // начало транзакции
                User user = new User (
                        login,
                        password,
                        UserRole.USER
                );
                int user_id = db.getUserDAO().addUser(user);
                Driver driver = new Driver (
                        firstName,
                        middleName,
                        lastName,
                        Integer.parseInt(ageText),
                        phone,
                        user_id
                );
                db.getDriverDAO().addDriver(driver);
                db.getConnection().commit();
                JOptionPane.showMessageDialog(this, "Пользователь успешно зарегистрирован!");
            } catch (SQLException ex) {
                // Например, если логин уже занят (уникальность)
                if (ex.getSQLState().equals("23505")) { // 23505 — уникальное ограничение нарушено в PostgreSQL
                    String msg = ex.getMessage();
                    if (msg.contains("un_login"))
                        JOptionPane.showMessageDialog(
                                this,
                                "Пользователь с таким логином уже существует."
                        );
                    if (msg.contains("driver_phone_number_key"))
                        JOptionPane.showMessageDialog(
                                this,
                                "Пользователь с таким номером телефона уже существует."
                        );
                } else if (ex.getSQLState().equals("23514")) {
                    String msg = ex.getMessage();
                    if (msg.contains("check_age"))
                        JOptionPane.showMessageDialog(
                                this,
                                "Возраст должен быть больше 18."
                        );
                    if (msg.contains("check_phone"))
                        JOptionPane.showMessageDialog(
                                this,
                                "Неправильный формат телефона."
                        );
                } else if (ex.getSQLState().equals("23502")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Заполните все обязательные поля."
                    );
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка регистрации: " + ex.getMessage());
                }
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Возраст должен быть числом.");
            } finally {
                try {
                    db.getConnection().rollback(); // сбрасываем коммиты
                    db.getConnection().setAutoCommit(true); // возвращаем автокоммиты
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                db.close();
            }
        });
    }
}
