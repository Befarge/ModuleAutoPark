package window.user;
import customException.InactionException;
import db.DatabaseConnection;

import entity.Car;
import entity.Driver;
import entity.User;

import types.SearchCriterionUser;

import types.UserRole;
import types.UserStatus;
import window.car.ListCarsAdminWindow;
import window.car.ViewCarWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ListUsersAdminWindow extends JDialog {
    private DatabaseConnection db;
    private List<User> users;
    private DefaultListModel<Driver> listModel;

    public ListUsersAdminWindow(Window parent, DatabaseConnection db) {
        super((Frame) parent, "Список пользователей", true);
        this.db = db;

        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void updateWindow() {
        users = db.getUserDAO().getAllUsers();
        listModel.clear();

        for (User user : users) {
            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
            listModel.addElement(driver);
        }
    }

    private void initUI() {
        // Панель для поиска
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Поле ввода
        JTextField searchField = new JTextField(15);

        JComboBox<SearchCriterionUser> searchCriteria = new JComboBox<>(SearchCriterionUser.values());

        // Кнопка поиска
        JButton searchButton = new JButton("Поиск");

        // Добавляем компоненты на панель
        searchPanel.add(searchField);
        searchPanel.add(searchCriteria);
        searchPanel.add(searchButton);

        // Список для вывода результатов
        listModel = new DefaultListModel<>();
        JList<Driver> resultList = new JList<>(listModel);

        users = db.getUserDAO().getAllUsers();
        for (User user : users) {
            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
            listModel.addElement(driver);
        }

        // Оборачиваем список в прокрутку
        JScrollPane scrollPane = new JScrollPane(resultList);

        // Нижняя панель с кнопками управления
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton infoButton = new JButton("Посмотреть информацию");
        JButton deleteButton = new JButton("Заблокировать");
        JButton addButton = new JButton("Принять запрос");
        JButton unblockButton = new JButton("Разблокировать");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Driver driver = resultList.getSelectedValue();
                if (driver != null) {
                    int confirm = JOptionPane.showConfirmDialog(ListUsersAdminWindow.this, "Заблокировать пользователя ?", "Подтвердите удаление", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            User user = db.getUserDAO().getUserById(driver.getUserId());
                            if (user.getRole() == UserRole.ADMIN) {
                                JOptionPane.showMessageDialog(
                                        ListUsersAdminWindow.this,
                                        "Данного пользователя  нельзя заблокировать.",
                                        "Ошибка", JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }
                            user.setStatus(UserStatus.BLOCKED);
                            db.getUserDAO().updateUser(user);
                            db.commit();
                        } catch (InactionException ex) {
                            JOptionPane.showMessageDialog(
                                    ListUsersAdminWindow.this,
                                    "Данная машина не существует.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE
                            );
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    ListUsersAdminWindow.this,
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
                    JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Сначала выберите юзера для блокировки.");
                }
                updateWindow();
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Driver driver = resultList.getSelectedValue();
                if (driver != null) {
                    if (db.getDriverDAO().getDriverById(driver.getId()) == null)
                        JOptionPane.showMessageDialog(
                                ListUsersAdminWindow.this,
                                "Данный пользователь не существует.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else
                        new InfoUserWindow(ListUsersAdminWindow.this, db, driver);
                } else {
                    JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Сначала выберите пользователя для просмотра.");
                }
                updateWindow();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWindow();
                String searchText = searchField.getText();
                SearchCriterionUser selectedCriterion = (SearchCriterionUser) searchCriteria.getSelectedItem();

                listModel.clear();
                switch (selectedCriterion) {
                    case LOGIN -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (user.getLogin().contains(searchText)) {
                                listModel.addElement(driver);
                            }
                        }
                    } case FIRSTNAME -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (driver.getFirstName().contains(searchText)) {
                                listModel.addElement(driver);
                            }
                        }
                    } case  MIDDLENAME -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (driver.getMiddleName().contains(searchText)) {
                                listModel.addElement(driver);
                            }
                        }
                    } case LASTNAME -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (driver.getLastName().contains(searchText)) {
                                listModel.addElement(driver);
                            }
                        }
                    } case ON_TRIP -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (driver.isOnTrip()) {
                                listModel.addElement(driver);
                            }
                        }
                    } case NO_ON_TRIP -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (!driver.isOnTrip()) {
                                listModel.addElement(driver);
                            }
                        }
                    } case WAIT -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (user.getStatus() == UserStatus.WAIT) {
                                listModel.addElement(driver);
                            }
                        }
                    } case BLOCKED -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (user.getStatus() == UserStatus.BLOCKED) {
                                listModel.addElement(driver);
                            }
                        }
                    } case CONFIRMED -> {
                        for (User user : users) {
                            Driver driver = db.getDriverDAO().getDriverByUserId(user.getId());
                            if (user.getStatus() == UserStatus.CONFIRMED) {
                                listModel.addElement(driver);
                            }
                        }
                    } case null -> {
                        JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Параметр не указан.");
                    }
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Driver driver = resultList.getSelectedValue();
                if (driver != null) {
                    User user = db.getUserDAO().getUserById(driver.getUserId());
                    if (db.getUserDAO().getUserById(user.getId()) == null)
                        JOptionPane.showMessageDialog(
                                ListUsersAdminWindow.this,
                                "Данный пользователь не существует.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else if (user.getStatus() != UserStatus.WAIT)
                        JOptionPane.showMessageDialog(
                                ListUsersAdminWindow.this,
                                "Данный пользователь не требует принятия.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else {
                        try {
                            user.setStatus(UserStatus.CONFIRMED);
                            db.getUserDAO().updateUser(user);
                            db.commit();
                            JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Пользователь принят.");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    ListUsersAdminWindow.this,
                                    "Неизвестная ошибка.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Сначала выберите юзера для принятия.");
                updateWindow();
            }
        });

        unblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Driver driver = resultList.getSelectedValue();
                if (driver != null) {
                    User user = db.getUserDAO().getUserById(driver.getUserId());
                    if (db.getUserDAO().getUserById(user.getId()) == null)
                        JOptionPane.showMessageDialog(
                                ListUsersAdminWindow.this,
                                "Данный пользователь не существует.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else if (user.getStatus() != UserStatus.BLOCKED)
                        JOptionPane.showMessageDialog(
                                ListUsersAdminWindow.this,
                                "Данный пользователь не требует разблокировки.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE
                        );
                    else {
                        try {
                            user.setStatus(UserStatus.CONFIRMED);
                            db.getUserDAO().updateUser(user);
                            db.commit();
                            JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Пользователь разблокирован.");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    ListUsersAdminWindow.this,
                                    "Неизвестная ошибка.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(ListUsersAdminWindow.this, "Сначала выберите юзера для разблокировки.");
                updateWindow();
            }
        });

        actionPanel.add(infoButton);
        actionPanel.add(deleteButton);
        actionPanel.add(addButton);
        actionPanel.add(unblockButton);

        // Добавляем панель на окно
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
}
