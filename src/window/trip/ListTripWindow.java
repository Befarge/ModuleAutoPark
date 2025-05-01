package window.trip;

import db.DatabaseConnection;

import entity.Trip;

import types.SearchCriterionTrip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListTripWindow extends JDialog{
    private DatabaseConnection db;
    private List<Trip> trips;
    private DefaultListModel<Trip> listModel;

    public ListTripWindow(Window parent, DatabaseConnection db) {
        super(parent, "История поездок", Dialog.ModalityType.APPLICATION_MODAL);
        this.db = db;

        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void updateWindow() {
        trips = db.getTripDAO().getAllTrip();
        listModel.clear();

        for (Trip trip : trips) {
            listModel.addElement(trip);
        }
    }

    private void initUI() {
        // Панель для поиска
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Поле ввода
        JTextField searchField = new JTextField(15);

        JComboBox<SearchCriterionTrip> searchCriteria = new JComboBox<>(SearchCriterionTrip.values());

        // Кнопка поиска
        JButton searchButton = new JButton("Поиск");

        // Добавляем компоненты на панель
        searchPanel.add(searchField);
        searchPanel.add(searchCriteria);
        searchPanel.add(searchButton);

        // Список для вывода результатов
        listModel = new DefaultListModel<>();
        JList<Trip> resultList = new JList<>(listModel);

        trips = db.getTripDAO().getAllTrip();
        for (Trip trip : trips) {
            listModel.addElement(trip);
        }

        // Оборачиваем список в прокрутку
        JScrollPane scrollPane = new JScrollPane(resultList);

        // Нижняя панель с кнопками управления
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton infoButton = new JButton("Посмотреть информацию");



        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Trip trip = resultList.getSelectedValue();
                if (trip != null) {
                    if (db.getTripDAO().getTripById(trip.getId()) == null)
                        JOptionPane.showMessageDialog(
                                ListTripWindow.this,
                                "Данная поездка не существует.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else
                        new InfoTripWindow(ListTripWindow.this, db, trip);
                } else {
                    JOptionPane.showMessageDialog(ListTripWindow.this, "Сначала выберите элемент для просмотра.");
                }
                updateWindow();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWindow();
                String searchText = searchField.getText();
                SearchCriterionTrip selectedCriterion = (SearchCriterionTrip) searchCriteria.getSelectedItem();

                listModel.clear();
                switch (selectedCriterion) {
                    case CAR -> {
                        for (Trip trip : trips) {
                            if (db.getCarDAO().getCarById(trip.getCarId()).getLicensePlate().contains(searchText))
                                listModel.addElement(trip);
                        }
                    } case DRIVER -> {
                        for (Trip trip : trips) {
                            if (db.getDriverDAO().getDriverById(trip.getDriverId()).getPhoneNumber().contains(searchText))
                                listModel.addElement(trip);
                        }
                    } case END -> {
                        for (Trip trip : trips) {
                            if (trip.getEndTime() != null)
                                listModel.addElement(trip);
                        }
                    } case NO_END -> {
                        for (Trip trip : trips) {
                            if (trip.getEndTime() == null)
                                listModel.addElement(trip);
                        }
                    } case null -> {
                        JOptionPane.showMessageDialog(ListTripWindow.this, "Параметр не указан.");
                    }
                }
            }
        });

        actionPanel.add(infoButton);

        // Добавляем панель на окно
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
}
