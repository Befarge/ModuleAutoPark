package window;

import db.DatabaseConnection;
import entity.Driver;
import entity.User;
import javax.swing.*;
import java.awt.*;

public class ProfileWindow extends JDialog {
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField phoneField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton changePassword;
    private JButton quitAccount;
    private DatabaseConnection db;
    private JFrame parent;
    private User user;

    public ProfileWindow(JFrame parent, DatabaseConnection db, User user) {
        super(parent, "Личный кабинет", true);
        this.parent = parent;
        this.user = user;
        this.db = db;
        setSize(400, 300);
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

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        changePassword = new JButton("Изменить пароль");
        changePassword.addActionListener(e -> clickChangePassword());
        add(changePassword);

        quitAccount = new JButton("Выйти из аккаунта");
        quitAccount.addActionListener(e -> clickQuitAccount());
        add(quitAccount);
    }

    private void onSave() {
        // Здесь будет логика сохранения изменений
        JOptionPane.showMessageDialog(this, "Данные сохранены");
        // Можно вызвать метод DAO для обновления данных в базе
    }

    private void clickChangePassword () {
        new ChangePasswordWindow(this, db, user);
    }

    private void clickQuitAccount () {
        new LoginWindow(db);
        parent.dispose();
        dispose();
    }
}

