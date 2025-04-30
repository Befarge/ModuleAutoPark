package window;
import db.DatabaseConnection;
import entity.User;
import types.SessionManager;
import window.car.ListCarsWindow;
import window.trip.CompleteTripWindow;
import window.user.LoginWindow;
import window.user.ProfileWindow;
import javax.swing.*;
import java.awt.*;


public class UserWindow extends JFrame {
    private DatabaseConnection db;

    public UserWindow(DatabaseConnection db) {
        this.db = db;

        setTitle("Главное окно");
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null); // центр экрана
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUser();
        setVisible(true);
    }

    private void initUser() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Кнопка "Личный кабинет"
        JButton personalCabinetButton = new JButton("Личный кабинет");
        personalCabinetButton.addActionListener(e -> {
            if (SessionManager.checkBlocked() && !SessionManager.checkOnTrip()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Вы заблокированы.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                new LoginWindow(db);
                dispose();
            } else if (SessionManager.checkBlocked() && SessionManager.checkOnTrip()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Данная функция недоступна, так как вы заблокированы. " +
                                "Завершите поездку и свяжитесь с администратором.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                callProfileWindow();
            }
        });
        panel.add(personalCabinetButton);

        //Кнопка "Взять машину"
        JButton listCarButton = new JButton("Взять машину");
        listCarButton.addActionListener(e -> {
            if (SessionManager.checkBlocked() && !SessionManager.checkOnTrip()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Вы заблокированы.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                new LoginWindow(db);
                dispose();
            } else if (SessionManager.checkBlocked() && SessionManager.checkOnTrip()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Данная функция недоступна, так как вы заблокированы. " +
                                "Завершите поездку и свяжитесь с администратором.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                callListCars();
            }
        });
        panel.add(listCarButton);

        //Кнопка "Завершить поездку"
        JButton endTheTripButton = new JButton("Завершить поездку");
        endTheTripButton.addActionListener(e -> {
            if (SessionManager.checkBlocked() && !SessionManager.checkOnTrip()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Вы заблокированы.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                new LoginWindow(db);
                dispose();
            } else {
                callCompleteTripWindow();
            }
        });
        panel.add(endTheTripButton);

        add(panel);
    }

    private void callProfileWindow() {
        new ProfileWindow(this, db, SessionManager.getUser());
    }

    private void callCompleteTripWindow () {
        if (SessionManager.checkOnTrip()) {
            new CompleteTripWindow(this, db, SessionManager.getUser());
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "У вас нет начатой поездки.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void callListCars() {
        if (!SessionManager.checkOnTrip()) {
            new ListCarsWindow(this, db, SessionManager.getUser());
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "У вас уже есть машина.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
