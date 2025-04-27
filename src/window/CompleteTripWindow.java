package window;

import db.DatabaseConnection;
import entity.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompleteTripWindow extends JDialog {
    private JTextField distanceField;
    private JTextField fuelLevelField;
    private JButton completeButton;
    private DatabaseConnection db;
    private User user;

    public CompleteTripWindow(JFrame parent, DatabaseConnection db, User user) {
        super(parent, "Завершение поездки", true);
        this.user = user;
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
        String distanceText = distanceField.getText().trim();
        String fuelText = fuelLevelField.getText().trim();

        if (distanceText.isEmpty() || fuelText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int distance = Integer.parseInt(distanceText);
            int fuel = Integer.parseInt(fuelText);

            if (distance <= 0 || fuel < 0 || fuel > 100) {
                JOptionPane.showMessageDialog(this, "Введите корректные значения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Здесь можно вставить код для обновления поездки в базе данных

            JOptionPane.showMessageDialog(this, "Поездка успешно завершена!");
            dispose(); // Закрыть окно после завершения
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введите числовые значения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
