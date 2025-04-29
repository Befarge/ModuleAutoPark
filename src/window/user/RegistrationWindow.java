package window.user;
import customException.*;
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
    private JFrame parentWindow;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private Class<NullException> nullExceptionClass;

    public RegistrationWindow(DatabaseConnection db, JFrame parentWindow) {
        this.db = db;
        this.parentWindow =  parentWindow;

        setTitle("Регистрация");
        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
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
        registerButton.addActionListener( e -> clickButtonRegistration());
        panel.add(registerButton);

        panel.add(new JLabel()); // пустая ячейка

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed (WindowEvent e) {
                if (parentWindow != null) {
                    parentWindow.setVisible(true);
                }
            }
        });

        add(panel);
    }

    private void clickButtonRegistration() {
        String firstName = StringUtils.trimToNull(firstNameField.getText());
        String middleName = StringUtils.trimToNull(middleNameField.getText());
        String lastName = StringUtils.trimToNull(lastNameField.getText());
        String ageText = StringUtils.trimToNull(ageField.getText());
        String phone = StringUtils.trimToNull(phoneField.getText());
        String login = StringUtils.trimToNull(loginField.getText());
        String password = StringUtils.trimToNull(new String(passwordField.getPassword()));

        try {
            User user = new User(
                    login,
                    password,
                    UserRole.USER
            );
            int user_id = db.getUserDAO().addUser(user);

            Driver driver = new Driver(
                    firstName,
                    middleName,
                    lastName,
                    Integer.parseInt(ageText),
                    phone,
                    user_id
            );
            db.getDriverDAO().addDriver(driver);

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
