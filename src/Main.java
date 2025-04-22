import dao.DriverDAO;
import entity.Driver;
import db.ConfigReader;
import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connect = db.connect();
        DriverDAO dao = new DriverDAO(connect);

        Driver driver1 = new Driver(
                "Евгений",
                "Александрович",
                "Веников",
                19,
                "89611722075",
                1
        );

        //dao.addDriver(driver1);
        //dao.getDriverById(2).printInfo();
        //dao.updateDriver(driver1, 2);
        //dao.deleteDriver(2);
        //dao.getDriverByUserId(1).printInfo();
        dao.getDriverByPhoneNumber("89611722076").printInfo();

        db.release();
        connect.close();
    }
}