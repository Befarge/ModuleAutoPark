import dao.UserDAO;
import db.ConfigReader;
import db.DatabaseConnection;
import entity.User;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        User user1 = new User("befarge", "gg17");
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connect = db.connect();
        UserDAO md = new UserDAO(connect);
        md.addUser(user1);
        db.release();
        connect.close();
    }
}