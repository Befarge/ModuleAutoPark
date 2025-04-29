package window.user;
import customException.NullException;
import db.DatabaseConnection;
import entity.User;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ChangePasswordWindow extends JDialog {
    private JButton saveButton;
    private JButton closeButton;
    private JPasswordField passwordField;
    private User user;
    private DatabaseConnection db;

    public ChangePasswordWindow (Window parent, DatabaseConnection db, User user) {
        super(parent, "Изменение пароля", ModalityType.APPLICATION_MODAL);
        this.user = user;
        this.db = db;
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new GridLayout(2, 2, 10, 5));

        add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        add(passwordField);

        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> onSave());
        add(saveButton);

        closeButton = new JButton("Отмена");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);
    }

    private void onSave() {
        String password = StringUtils.trimToNull(new String(passwordField.getPassword()));
        User temp_user = db.getUserDAO().getUserById(user.getId());
        temp_user.setPassword(password);

        try {
            db.getUserDAO().updateUser(temp_user);
            db.commit();
            user.setPassword(password);
            JOptionPane.showMessageDialog(this, "Пароль изменен.");
            dispose();
        } catch (NullException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Заполните все обязательные данные.",
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
