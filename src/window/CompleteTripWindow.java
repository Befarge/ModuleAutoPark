package window;

import customException.*;
import db.DatabaseConnection;
import entity.Car;
import entity.Driver;
import entity.Trip;
import entity.User;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CompleteTripWindow extends JDialog {
    private JTextField distanceField;
    private JTextField fuelLevelField;
    private JButton completeButton;
    private DatabaseConnection db;
    private User user;

    public CompleteTripWindow(JFrame parent, DatabaseConnection db, User user) {
        super(parent, "Завершение поездки", true);
        this.user = user;
        this.db = db;
        setSize(300, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 5));

        fieldsPanel.add(new JLabel("Расстояние (км):"));
        distanceField = new JTextField();
        fieldsPanel.add(distanceField);

        fieldsPanel.add(new JLabel("Топливо (%):"));
        fuelLevelField = new JTextField();
        fieldsPanel.add(fuelLevelField);
        add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        completeButton = new JButton("Подтвердить");
        buttonPanel.add(completeButton);
        completeButton.addActionListener(e -> onCompleteTrip());

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onCompleteTrip() {
        String distanceText = StringUtils.trimToNull(distanceField.getText());
        String fuelText = StringUtils.trimToNull(fuelLevelField.getText());

        if (distanceText == null || fuelText == null) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int distance = Integer.parseInt(distanceText);
            int fuel = Integer.parseInt(fuelText);
            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
            Trip trip = db.getTripDAO().getTripByDriverId(driver.getId());

            db.getTripDAO().finishTrip(distance, fuel, trip.getId());
            driver.setOnTrip(false);
            Car car = db.getCarDAO().getCarById(trip.getCarId());
            car.setAvailable(true);
            db.getDriverDAO().updateDriver(driver);
            db.getCarDAO().updateCar(car);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Вы уверены, что хотите завершить поездку?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                db.commit();
                JOptionPane.showMessageDialog(
                        this,
                        "Поездка успешно завершена!"
                );
                dispose(); // Закрыть окно после завершения
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Введите числовые значения.",
                    "Ошибка", JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckDistanceException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Расстояние не может быть ниже 0.",
                    "Ошибка", JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckFuelException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Топливо не может быть ниже 0.",
                    "Ошибка", JOptionPane.ERROR_MESSAGE
            );
        } catch (CheckException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Какие-то данные не проходят проверку.",
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
