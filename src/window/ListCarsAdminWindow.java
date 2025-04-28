package window;
import db.DatabaseConnection;
import entity.Car;
import types.SearchCriterionCar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ListCarsAdminWindow extends JDialog {
    private DatabaseConnection db;

    public ListCarsAdminWindow(Window parent, DatabaseConnection db) {
        super((Frame) parent, "Список машин", true);
        this.db = db;

        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        // Панель для поиска
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Поле ввода
        JTextField searchField = new JTextField(15);

        JComboBox<SearchCriterionCar> searchCriteria = new JComboBox<>(SearchCriterionCar.values());

        // Кнопка поиска
        JButton searchButton = new JButton("Поиск");

        // Добавляем компоненты на панель
        searchPanel.add(searchField);
        searchPanel.add(searchCriteria);
        searchPanel.add(searchButton);

        // Список для вывода результатов
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> resultList = new JList<>(listModel);

        List<Car> cars = db.getCarDAO().getAllCars();
        for (Car car : cars) {
            listModel.addElement("Модель: " + car.getModel() + ", Номер: " + car.getLicensePlate());
        }

        // Оборачиваем список в прокрутку
        JScrollPane scrollPane = new JScrollPane(resultList);

        // Нижняя панель с кнопками управления
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton infoButton = new JButton("Посмотреть информацию");
        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = resultList.getSelectedIndex();
                if (selectedIndex != -1) {
                    int confirm = JOptionPane.showConfirmDialog(ListCarsAdminWindow.this, "Удалить выбранный элемент?", "Подтвердите удаление", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            int car_id = cars.get(selectedIndex).getId();
                            db.getTripDAO().deleteTripByCar(car_id);
                            db.getCarDAO().deleteCar(car_id);
                            listModel.remove(selectedIndex);
                            db.commit();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    ListCarsAdminWindow.this,
                                    "Что-то пошло не так.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE
                            );
                        } finally {
                            try {
                                db.getConnection().rollback();
                            } catch (SQLException exp) {
                                exp.printStackTrace();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(ListCarsAdminWindow.this, "Сначала выберите элемент для удаления.");
                }
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = resultList.getSelectedIndex();
                if (selectedIndex != -1) {
                    new ViewCarWindow(ListCarsAdminWindow.this, db, cars.get(selectedIndex));
                } else {
                    JOptionPane.showMessageDialog(ListCarsAdminWindow.this, "Сначала выберите элемент для удаления.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                SearchCriterionCar selectedCriterion = (SearchCriterionCar) searchCriteria.getSelectedItem();

                listModel.clear();
                switch (selectedCriterion) {
                    case AVAILABLE -> {
                        for (Car car : cars) {
                            if (car.isAvailable())
                                listModel.addElement("Модель: " + car.getModel() + ", Номер: " + car.getLicensePlate());
                        }
                    } case NO_AVAILABLE -> {
                        for (Car car : cars) {
                            if (!car.isAvailable())
                                listModel.addElement("Модель: " + car.getModel() + ", Номер: " + car.getLicensePlate());
                        }
                    } case  MODEL -> {
                        for (Car car : cars) {
                            if (car.getModel().contains(searchText))
                                listModel.addElement("Модель: " + car.getModel() + ", Номер: " + car.getLicensePlate());
                        }
                    } case FUEL -> {
                        try {
                            int number = Integer.parseInt(searchText);
                            if (number < 0) {
                                JOptionPane.showMessageDialog(
                                        ListCarsAdminWindow.this,
                                        "Не может быть ниже 0.",
                                        "Ошибка", JOptionPane.ERROR_MESSAGE
                                );
                            }
                            for (Car car : cars) {
                                if (car.getFuelLevel() <= number)
                                    listModel.addElement("Модель: " + car.getModel() + ", Номер: " + car.getLicensePlate());
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    ListCarsAdminWindow.this,
                                    "Укажите число.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } case null -> {
                        JOptionPane.showMessageDialog(ListCarsAdminWindow.this, "Параметр не указан.");
                    }
                }
            }
        });


        actionPanel.add(infoButton);
        actionPanel.add(deleteButton);

        // Добавляем панель на окно
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
}
