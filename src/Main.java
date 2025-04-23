import dao.CarDAO;
import db.ConfigReader;
import db.DatabaseConnection;
import entity.Car;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connect = db.connect();
        CarDAO md = new CarDAO(connect);

        Car car1 = new Car(
                "Toyta Camri",
                "О777АА136",
                1000,
                50,
                "2025-04-15"
        );

//        md.addCar(car1);
//        Car new_car = md.getCarById(2);
//        new_car.setFuelLevel(40);
//        new_car.setMileage(500);
//        md.updateCar(new_car, 2);
//        md.deleteCar(2);

        md.getCarByLicensePlate("О170СМ31").printInfo();

        db.release();
        connect.close();
    }
}