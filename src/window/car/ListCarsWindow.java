package window.car;
import db.DatabaseConnection;
import entity.Car;
import entity.Driver;
import entity.Trip;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class ListCarsWindow extends JDialog {
    private DatabaseConnection db;
    private List<Car> cars;
    private DefaultListModel<String> listModel;
    private JList<String> carList;
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

        // Инициализация модели для JList
        listModel = new DefaultListModel<>();
        for (Car car : cars) {
            listModel.addElement("Модель: " + car.getModel() + ", Номер: " + car.getLicensePlate());
        }

        // Создание JList
        carList = new JList<>(listModel);
        carList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Разрешаем выбирать только одну машину
        carList.setVisibleRowCount(10); // Количество видимых строк

        // Добавление прокрутки
        JScrollPane scrollPane = new JScrollPane(carList);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        // Панель для списка
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Выберите машину:"), BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Кнопка "Выбрать"
        JButton selectButton = new JButton("Выбрать");
        selectButton.addActionListener(e -> selectedCar());

        // Панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(selectButton);

        // Основная компоновка
        setLayout(new BorderLayout(10, 10));
        add(listPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void selectedCar() {
        int selectedIndex = carList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Пожалуйста, выберите машину",
                    "Предупреждение",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
