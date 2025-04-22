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
        User user1 = new User("venikov", "1234", UserRole.USER);
        //md.addUser(user1);
        //md.deleteUser(10);
        //System.out.println(md.getUserById(11).getLogin());
        //System.out.println(md.getUserByLogin("venikov").getPassword());
        db.release();
        connect.close();
    }
}