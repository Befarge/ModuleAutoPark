import db.ConfigReader;
import db.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);

        db.getDriverDAO().getDriverById(4).printInfo();
        db.close();
    }
}