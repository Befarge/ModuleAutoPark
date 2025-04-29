package window.user;

import customException.*;
import db.DatabaseConnection;
import entity.Driver;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import window.car.ViewCarWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ProfileWindow extends JDialog {
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JButton saveButton;
    private JButton changePassword;
    private JButton quitAccount;
    private JButton viewCar;
    private DatabaseConnection db;
    private JFrame parent;
    private User user;

    public ProfileWindow(JFrame parent, DatabaseConnection db, User user) {
        super(parent, "Личный кабинет", true);
        this.parent = parent;
        this.user = user;
        this.db = db;
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());

        setLayout(new GridLayout(7, 2, 10, 5));

        add(new JLabel("Имя:"));
        firstNameField = new JTextField(driver.getFirstName());
        add(firstNameField);

        add(new JLabel("Отчество:"));
        middleNameField = new JTextField(driver.getMiddleName());
        add(middleNameField);

        add(new JLabel("Фамилия:"));
        lastNameField = new JTextField(driver.getLastName());
        add(lastNameField);

        add(new JLabel("Возраст:"));
        ageField = new JTextField(String.valueOf(driver.getAge()));
        add(ageField);

        add(new JLabel("Телефон:"));
        phoneField = new JTextField(driver.getPhoneNumber());
        add(phoneField);

        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> onSave());
        add(saveButton);

        changePassword = new JButton("Изменить пароль");
        changePassword.addActionListener(e -> clickChangePassword());
        add(changePassword);

        quitAccount = new JButton("Выйти из аккаунта");
        quitAccount.addActionListener(e -> clickQuitAccount());
        add(quitAccount);

        viewCar = new JButton("Текущая машина");
        viewCar.addActionListener(e -> clickViewCar());
        add(viewCar);
    }

    private void onSave() {
        String firstName = StringUtils.trimToNull(firstNameField.getText());
        String middleName = StringUtils.trimToNull(middleNameField.getText());
        String lastName = StringUtils.trimToNull(lastNameField.getText());
        String ageText = StringUtils.trimToNull(ageField.getText());
        String phone = StringUtils.trimToNull(phoneField.getText());

        try {
            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
            driver.setFirstName(firstName);
            driver.setMiddleName(middleName);
            driver.setLastName(lastName);
            driver.setAge(Integer.parseInt(ageText));
            driver.setPhoneNumber(phone);

            db.getDriverDAO().updateDriver(driver);
            db.commit();

            JOptionPane.showMessageDialog(this, "Данные сохранены");
            dispose();
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

    private void clickChangePassword () {
        new ChangePasswordWindow(this, db, user);
    }

    private void clickQuitAccount () {
        new LoginWindow(db);
        parent.dispose();
        dispose();
    }

    private void clickViewCar () {
        boolean isHaveCar = db.getDriverDAO().getDriverByUserId(user.getId()).isOnTrip();
        if (isHaveCar)
            new ViewCarWindow(
                    this,
                    db,
                    db.getCarDAO().getCarById(
                            db.getTripDAO().getTripByDriverId(
                                    db.getDriverDAO().getDriverByUserId(
                                            user.getId()
                                    ).getId()
                            ).getCarId()
                    )
            );
        else
            JOptionPane.showMessageDialog(
                    this,
                    "Вы еще не выбрали машину для поездки.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
    }
}

