package window.trip;

import db.DatabaseConnection;
import entity.Car;
import entity.Trip;
import window.car.ViewCarWindow;
import window.user.InfoUserWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InfoTripWindow extends JDialog {
    private DatabaseConnection db;
    private Trip trip;

    public InfoTripWindow(JDialog parent, DatabaseConnection db, Trip trip) {
        super(parent, "Информация о машине", true);
        this.trip = trip;
        this.db = db;
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new GridLayout(5, 2, 10, 5));
        JButton infoUser = new JButton("Водитель");
        JButton infoCar = new JButton("Машина");

        add(new JLabel("Начало поездки:"));
        add(new JLabel(trip.getStartTime()));

        add(new JLabel("Конец поездки:"));
        add(new JLabel(trip.getEndTime()));

        add(new JLabel("Расстояние:"));
        add(new JLabel(String.valueOf(trip.getDistance())));

        add(new JLabel("Оставшееся топливо (л):"));
        add(new JLabel(String.valueOf(trip.getFuelUsed())));

        infoUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InfoUserWindow(
                        InfoTripWindow.this,
                        db,
                        db.getDriverDAO().getDriverById(trip.getDriverId())
                );
            }
        });

        infoCar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewCarWindow(InfoTripWindow.this, db);
            }
        });

        add(infoUser);
        add(infoCar);
    }
}