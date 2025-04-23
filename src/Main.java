import dao.TripDAO;
import db.ConfigReader;
import db.DatabaseConnection;
import entity.Trip;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connect = db.connect();
        TripDAO md = new TripDAO(connect);

        Trip trip1 = new Trip(
                4,
                1
        );

//        md.addTrip(trip1);
//        Trip new_trip = md.getTripById(4);
//        md.deleteTrip(3);
//        new_trip.setFuelUsed(49);
//        new_trip.setDistance(100);
//
//        md.updateTrip(new_trip,4);

        md.getTripByDriverId(4).printInfo();

        db.release();
        connect.close();
    }
}