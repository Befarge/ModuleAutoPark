package window.car;
import customException.InactionException;
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
    private List<Car> cars;
    private DefaultListModel<Car> listModel;

    public ListCarsAdminWindow(Window parent, DatabaseConnection db) {
        super((Frame) parent, "Список машин", true);
        this.db = db;

        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
        JButton infoButton = new JButton("Посмотреть информацию");
        JButton deleteButton = new JButton("Удалить");
        JButton editButton = new JButton("Редактировать");
        JButton addButton = new JButton("Добавить машину");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Car car = resultList.getSelectedValue();
                if (car != null) {
                    int confirm = JOptionPane.showConfirmDialog(ListCarsAdminWindow.this, "Удалить выбранный элемент?", "Подтвердите удаление", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            int car_id = car.getId();
                            db.getTripDAO().deleteTripByCar(car_id);
                            db.getCarDAO().deleteCar(car_id);
                            db.commit();
                        } catch (InactionException ex) {
                            JOptionPane.showMessageDialog(
                                    ListCarsAdminWindow.this,
                                    "Данная машина не существует.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE
                            );
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
                updateWindow();
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Car car = resultList.getSelectedValue();
                if (car != null) {
                    if (db.getCarDAO().getCarById(car.getId()) == null)
                        JOptionPane.showMessageDialog(
                                ListCarsAdminWindow.this,
                                "Данная машина не существует.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else
                        new ViewCarWindow(ListCarsAdminWindow.this, db, car);
                } else {
                    JOptionPane.showMessageDialog(ListCarsAdminWindow.this, "Сначала выберите элемент для просмотра.");
                }
                updateWindow();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWindow();
                String searchText = searchField.getText();
                SearchCriterionCar selectedCriterion = (SearchCriterionCar) searchCriteria.getSelectedItem();

                listModel.clear();
                switch (selectedCriterion) {
                    case AVAILABLE -> {
                        for (Car car : cars) {
                            if (car.isAvailable())
                                listModel.addElement(car);
                        }
                    } case NO_AVAILABLE -> {
                        for (Car car : cars) {
                            if (!car.isAvailable())
                                listModel.addElement(car);
                        }
                    } case  MODEL -> {
                        for (Car car : cars) {
                            if (car.getModel().contains(searchText))
                                listModel.addElement(car);
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
                                    listModel.addElement(car);
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Car car = resultList.getSelectedValue();
                if (car != null) {
                    if (db.getCarDAO().getCarById(car.getId()) == null)
                        JOptionPane.showMessageDialog(
                                ListCarsAdminWindow.this,
                                "Данная машина не существует.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else {
                        new EditCarWindow(ListCarsAdminWindow.this, db, car);
                    }
                } else {
                    JOptionPane.showMessageDialog(ListCarsAdminWindow.this, "Сначала выберите элемент для редактирования.");
                }
                updateWindow();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCarWindow(ListCarsAdminWindow.this, db);
                updateWindow();
            }
        });

        actionPanel.add(infoButton);
        actionPanel.add(deleteButton);
        actionPanel.add(editButton);
        actionPanel.add(addButton);

        // Добавляем панель на окно
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
}
