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
import java.util.List;

public class AddCarWindow extends JDialog {
    private DatabaseConnection db;
    private JTextField modelField;
    private JTextField licensePlateField;
    private JTextField mileageField;
    private JTextField fuelLevelField;
    private JTextField lastMaintenanceDateField;
    private JButton saveButton;

    public AddCarWindow(JDialog parent, DatabaseConnection db, List<Car> cars, DefaultListModel<String> listModel) {
        super(parent, "Добавление машины", true);
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
        modelField = new JTextField();
        infoPanel.add(modelField);

        infoPanel.add(new JLabel("Номер:"));
        licensePlateField = new JTextField();
        infoPanel.add(licensePlateField);

        infoPanel.add(new JLabel("Пробег:"));
        mileageField = new JTextField();
        infoPanel.add(mileageField);

        infoPanel.add(new JLabel("Топливо (л):"));
        fuelLevelField = new JTextField();
        infoPanel.add(fuelLevelField);

        infoPanel.add(new JLabel("Последнее ТО:"));
        lastMaintenanceDateField = new JTextField();
        infoPanel.add(lastMaintenanceDateField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Добавить");
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
                    Car car = new Car(
                            model,
                            licensePlate,
                            Integer.parseInt(mileage),
                            Integer.parseInt(fuelLevel),
                            lastMaintenanceDate
                    );

                    db.getCarDAO().addCar(car);

                    int result = JOptionPane.showConfirmDialog(
                            AddCarWindow.this,
                            "Вы уверены, что хотите добавить машину ?",
                            "Подтверждение",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (result == JOptionPane.NO_OPTION)
                        return;

                    db.commit();
                    dispose();
                } catch (CheckDateException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Неправильный формат даты.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckFuelException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Уровень топлива должен начинаться от 0.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckDistanceException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Пробег должен начинаться от 0.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckLicensePlateException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Неправильный формат номера.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (CheckException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Ошибка проверки.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (UniquenessLicensePlateException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Такой номер уже существует.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                catch (UniquenessException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Ошибка уникальности.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
                            "Неизвестная ошибка.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            AddCarWindow.this,
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
