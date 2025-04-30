package window.user;
import customException.*;
import db.DatabaseConnection;
import entity.Driver;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import types.UserRole;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegistrationWindow extends JDialog {
    private final DatabaseConnection db;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField loginField;
    private JPasswordField passwordField;

    public RegistrationWindow(DatabaseConnection db, JFrame parent) {
        super(parent, "Регистрация", ModalityType.APPLICATION_MODAL);

        this.db = db;

        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        JPanel actionPanel = new JPanel(new FlowLayout());

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

        JButton registerButton = new JButton("Зарегистрироваться");
        registerButton.addActionListener(e -> registrations());
        actionPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void registrations() {
        try {
            String firstName = StringUtils.trimToNull(firstNameField.getText());
            String middleName = StringUtils.trimToNull(middleNameField.getText());
            String lastName = StringUtils.trimToNull(lastNameField.getText());
            int age = Integer.parseInt(StringUtils.trimToNull(ageField.getText()));
            String phone = StringUtils.trimToNull(phoneField.getText());
            String login = StringUtils.trimToNull(loginField.getText());
            String password = StringUtils.trimToNull(new String(passwordField.getPassword()));

            int user_id = db.getUserDAO().addUser(
                    new User (
                        login,
                        password,
                        UserRole.USER
                    )
            );

            db.getDriverDAO().addDriver(
                    new Driver(
                        firstName,
                        middleName,
                        lastName,
                        age,
                        phone,
                        user_id
                    )
            );

            db.commit();
            JOptionPane.showMessageDialog(this, "Вы успешно зарегистрировались!");
            dispose();
        } catch (UniquenessLoginException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Пользователь с таким логином уже существует.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (UniquenessPhoneException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Пользователь с таким номером телефона уже существует.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (UniquenessException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ошибка в уникальности данных.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckAgeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Возраст должен быть от 18.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckPhoneException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Неправильный формат телефона.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckFmlException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "В ФИО должны быть только буквы.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Какие-то данные не проходят проверку.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NullException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Заполните все обязательные данные.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Возраст должен быть числом.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Неизвестная ошибка.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            try {
                db.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
