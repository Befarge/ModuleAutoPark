package types;

import db.DatabaseConnection;
import entity.Driver;
import entity.User;
import window.user.LoginWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecureActionListener implements ActionListener {
    private final ActionListener delegate;
    private User user;
    private DatabaseConnection db;

    public SecureActionListener (User user, DatabaseConnection db, ActionListener delegate) {
        this.user = user;
        this.db = db;
        this.delegate = delegate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        User user = db.getUserDAO().getUserById(this.user.getId());
        Driver driver = db.getDriverDAO().getDriverByUserId(this.user.getId());

        if (user.getStatus() == UserStatus.BLOCKED && !driver.isOnTrip()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ваша учётная запись заблокирована. Действие запрещено.",
                    "Блокировка", javax.swing.JOptionPane.ERROR_MESSAGE
            );

            for (Window window : Window.getWindows()) {
                window.dispose();
            }

            new LoginWindow(db);
        } else if (user.getStatus() == UserStatus.BLOCKED && driver.isOnTrip()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ваша учётная запись заблокирована. Завершите поездку и свяжитесь с администратором.",
                    "Блокировка", javax.swing.JOptionPane.ERROR_MESSAGE
            );

        } else {
            delegate.actionPerformed(e); // Выполняем действие
        }
    }
}
