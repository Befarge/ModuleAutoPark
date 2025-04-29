package window.user;

import db.DatabaseConnection;
import entity.Car;
import entity.Driver;

import javax.swing.*;
import java.awt.*;

public class InfoUserWindow extends JDialog {
    private DatabaseConnection db;
    private Driver driver;

    public InfoUserWindow(JDialog parent, DatabaseConnection db, Driver driver) {
        super(parent, "Информация о машине", true);
        this.driver = driver;
        this.db = db;
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new GridLayout(6, 2, 10, 5));

        add(new JLabel("Имя:"));
        add(new JLabel(driver.getFirstName()));

        add(new JLabel("Отчество:"));
        add(new JLabel(driver.getMiddleName()));

        add(new JLabel("Фамилия:"));
        add(new JLabel(driver.getLastName()));

        add(new JLabel("Возраст :"));
        add(new JLabel(String.valueOf(driver.getAge())));

        add(new JLabel("Номер:"));
        add(new JLabel(driver.getPhoneNumber()));

        add(new JLabel("Логин:"));
        add(new JLabel(db.getUserDAO().getUserById(driver.getUserId()).getLogin()));
    }
}