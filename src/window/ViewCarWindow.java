package window;
import db.DatabaseConnection;
import entity.Car;
import entity.User;

import javax.swing.*;
import java.awt.*;

public class ViewCarWindow extends JDialog {
    private  DatabaseConnection db;
    private User user;

    public ViewCarWindow(JDialog parent, DatabaseConnection db, User user) {
        super(parent, "Информация о машине", true);
        this.user = user;
        this.db = db;
        setSize(400, 300);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        Car car = db.getCarDAO().getCarById(
                db.getTripDAO().getTripByDriverId(
                        db.getDriverDAO().getDriverByUserId(
                                user.getId()
                        ).getId()
                ).getCarId()
        );

        setLayout(new GridLayout(5, 2, 10, 5));

        add(new JLabel("Модель:"));
        add(new JLabel(car.getModel()));

        add(new JLabel("Номер:"));
        add(new JLabel(car.getLicensePlate()));

        add(new JLabel("Пробег:"));
        add(new JLabel(String.valueOf(car.getMileage())));

        add(new JLabel("Топливо (л):"));
        add(new JLabel(String.valueOf(car.getFuelLevel())));

        add(new JLabel("Последнее ТО:"));
        add(new JLabel(car.getLastMaintenanceDate()));
        System.out.println("hello");
    }
}
