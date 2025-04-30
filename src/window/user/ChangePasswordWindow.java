package window.user;
import customException.NullException;
import db.DatabaseConnection;
import org.apache.commons.lang3.StringUtils;
import types.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ChangePasswordWindow extends JDialog {
    private JPasswordField passwordField;
    private DatabaseConnection db;

    public ChangePasswordWindow (Window parent, DatabaseConnection db) {
        super(parent, "Изменение пароля", ModalityType.APPLICATION_MODAL);
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

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> onSave());
        add(saveButton);

        JButton closeButton = new JButton("Отмена");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);
    }

    private void onSave() {
        try {
            String password = StringUtils.trimToNull(new String(passwordField.getPassword()));
            db.getUserDAO().updateUser(SessionManager.getCopyUser().setPassword(password));

            db.commit();
            SessionManager.getUser().setPassword(password);
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
        }
    }
}
