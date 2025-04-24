package window;

import dao.DriverDAO;
import dao.UserDAO;
import entity.Driver;
import entity.User;
import support.UserValidator;
import types.UserRole;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegistrationWindow extends JFrame {
    private final UserDAO userDAO;
    private final DriverDAO driverDAO;

    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationWindow(UserDAO userDAO, DriverDAO driverDAO) {
        this.userDAO = userDAO;
        this.driverDAO = driverDAO;

        setTitle("Регистрация");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String ageText = ageField.getText();
            String phone = phoneField.getText();
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());

            // Проверка пустых полей
            if (!UserValidator.areFieldsNotEmpty(firstName, lastName, login, password, ageText, phone)) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, заполните все обязательные поля.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Возраст должен быть числом.");
                return;
            }

            // Проверка возраста
            if (!UserValidator.isValidAge(age)) {
                JOptionPane.showMessageDialog(this, "Возраст должен быть не меньше 18 лет.");
                return;
            }

            // Проверка телефона
            if (!UserValidator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Телефон должен начинаться с 89 и содержать 11 цифр.");
                return;
            }

            // Всё ок — можно регистрировать
            try {
                User user = new User (
                        login,
                        password,
                        UserRole.USER
                );
                int user_id = userDAO.addUser(user);
                Driver driver = new Driver (
                        firstName,
                        middleName,
                        lastName,
                        age,
                        phone,
                        user_id
                );
                driverDAO.addDriver(driver);
                JOptionPane.showMessageDialog(this, "Пользователь успешно зарегистрирован!");
            } catch (SQLException ex) {
                // Например, если логин уже занят (уникальность)
                if (ex.getSQLState().equals("23505")) { // 23505 — уникальное ограничение нарушено в PostgreSQL
                    JOptionPane.showMessageDialog(this, "Пользователь с таким логином уже существует.");
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка регистрации: " + ex.getMessage());
                }
                ex.printStackTrace();
            }

        });
    }
}
