import dao.UserDAO;
import db.ConfigReader;
import db.DatabaseConnection;
import entity.User;
import types.UserRole;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connect = db.connect();
        UserDAO md = new UserDAO(connect);
        User user1 = new User("nekr", "2110", UserRole.MANAGER);
        //md.addUser(user1);
        //md.deleteUser(6);
        //System.out.println(md.getUserById(11).getLogin());
        //md.getUserByLogin("venikov").printInfo();

        System.out.println(md.addUser(user1));

        db.release();
        connect.close();
    }
}