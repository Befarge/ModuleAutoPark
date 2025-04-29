package window.car;

import customException.*;
import db.DatabaseConnection;
import entity.Car;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EditCarWindow extends JDialog {
    private DatabaseConnection db;
    private Car car;
    private JTextField modelField;
    private JTextField licensePlateField;
    private JTextField mileageField;
    private JTextField fuelLevelField;
    private JTextField lastMaintenanceDateField;
    private JButton saveButton;

    public EditCarWindow(JDialog parent, DatabaseConnection db, Car car) {
        super(parent, "Редактирование машины", true);
        this.car = car;
        this.db = db;
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 5));

        infoPanel.add(new JLabel("Модель:"));
        modelField = new JTextField(car.getModel());
        infoPanel.add(modelField);

        infoPanel.add(new JLabel("Номер:"));
        licensePlateField = new JTextField(car.getLicensePlate());
        infoPanel.add(licensePlateField);

        infoPanel.add(new JLabel("Пробег:"));
        mileageField = new JTextField(String.valueOf(car.getMileage()));
        infoPanel.add(mileageField);

        infoPanel.add(new JLabel("Топливо (л):"));
        fuelLevelField = new JTextField(String.valueOf(car.getFuelLevel()));
        infoPanel.add(fuelLevelField);

        infoPanel.add(new JLabel("Последнее ТО:"));
        lastMaintenanceDateField = new JTextField(car.getLastMaintenanceDate());
        infoPanel.add(lastMaintenanceDateField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Сохранить");
        buttonPanel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String model = StringUtils.trimToNull(modelField.getText());
                String licensePlate = StringUtils.trimToNull(licensePlateField.getText());
                String mileage = StringUtils.trimToNull(mileageField.getText());
                String fuelLevel = StringUtils.trimToNull(fuelLevelField.getText());
                String lastMaintenanceDate = StringUtils.trimToNull(lastMaintenanceDateField.getText());

                try {
                    Car copy_car = new Car(car);
                    copy_car.setModel(model);
                    copy_car.setLicensePlate(licensePlate);
                    copy_car.setMileage(Integer.parseInt(mileage));
                    copy_car.setFuelLevel(Integer.parseInt(fuelLevel));
                    copy_car.setLastMaintenanceDate(lastMaintenanceDate);
                    db.getCarDAO().updateCar(copy_car);

                    int result = JOptionPane.showConfirmDialog(
                            EditCarWindow.this,
                            "Вы уверены, что хотите изменить данные ?",
                            "Подтверждение",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (result == JOptionPane.NO_OPTION) {
                        return;
                    }

                    car.setModel(copy_car.getModel());
                    car.setLicensePlate(copy_car.getLicensePlate());
                    car.setMileage(copy_car.getMileage());
                    car.setFuelLevel(copy_car.getFuelLevel());
                    car.setLastMaintenanceDate(copy_car.getLastMaintenanceDate());

                    db.commit();
                    dispose();
                } catch (CheckDateException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Неправильный формат даты.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckFuelException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Уровень топлива должен начинаться от 0.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckDistanceException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Пробег должен начинаться от 0.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckLicensePlateException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Неправильный формат номера.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Ошибка проверки.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (UniquenessLicensePlateException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Такой номер уже существует.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                catch (UniquenessException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Ошибка уникальности.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Неизвестная ошибка.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            EditCarWindow.this,
                            "Ожидалось число.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                finally {
                    try {
                        db.getConnection().rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
