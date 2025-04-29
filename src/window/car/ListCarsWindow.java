package window.car;
import customException.InactionException;
import db.DatabaseConnection;
import entity.Car;
import entity.Driver;
import entity.Trip;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;


public class ListCarsWindow extends JDialog {
    private DatabaseConnection db;
    private List<Car> cars;
    private DefaultListModel<Car> listModel;
    private User user;


    public ListCarsWindow(JFrame parent, DatabaseConnection db, User user) {
        super(parent, "Выбор машины", true);
        this.db = db;
        this.user = user;
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void updateWindow() {
        cars = db.getCarDAO().getAllCars();
        listModel.clear();

        for (Car car : cars) {
            listModel.addElement(car);
        }
    }

    private void initUI () {
        // Получение списка машин из базы данных
        cars = db.getCarDAO().getAllCars(); // Предполагается, что метод возвращает List<Car>
        if (cars == null || cars.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Машины не найдены",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE)
            ;
            dispose();
            return;
        }

        listModel = new DefaultListModel<>();
        JList<Car> resultList = new JList<>(listModel);

        cars = db.getCarDAO().getAllCars();
        for (Car car : cars) {
            listModel.addElement(car);
        }

        // Оборачиваем список в прокрутку
        JScrollPane scrollPane = new JScrollPane(resultList);

        // Нижняя панель с кнопками управления
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton infoButton = new JButton("Выбрать");
        actionPanel.add(infoButton);

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = resultList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(
                            ListCarsWindow.this,
                            "Пожалуйста, выберите машину",
                            "Предупреждение",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int result = JOptionPane.showConfirmDialog(
                        ListCarsWindow.this,
                        "Вы точно хотите выбрать эту машину ?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.NO_OPTION) {
                    return;
                }

                Car car = cars.get(selectedIndex);
                Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                try {
                    db.getTripDAO().addTrip(new Trip(
                            driver.getId(),
                            car.getId()
                    ));
                    car.setAvailable(false);
                    driver.setOnTrip(true);

                    db.getCarDAO().updateCar(car);
                    db.getDriverDAO().updateDriver(driver);

                    db.commit();
                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        db.getConnection().rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                updateWindow();
            }
        });

        // Добавляем панель на окно
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
}
