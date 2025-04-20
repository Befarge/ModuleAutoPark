import dao.UserDAO;
import db.ConfigReader;
import db.DatabaseConnection;
import entity.User;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigReader config = new ConfigReader("config.properties");
        DatabaseConnection db = new DatabaseConnection(config);
        Connection connect = db.connect();
        UserDAO md = new UserDAO(connect);
//        User user1 = md.getUserById(2);
//        if (user1 != null) {
//            System.out.println(user1.getId());
//            System.out.println(user1.getPassword());
//            System.out.println(user1.getLogin());
//        } else {
//            System.out.println("юзер не найден");
//        }
        md.deleteUser(2);
        db.release();
        connect.close();
    }
}