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
        //User user1 = new User("venikov", "ven741", UserRole.ADMIN);
        //md.addUser(user1);
        //md.deleteUser(10);
        //System.out.println(md.getUserById(11).getLogin());
        //md.getUserByLogin("venikov").printInfo();
        User user1 = md.getUserByLogin("venikov");

        if (user1 != null) {
            user1.setLogin("befarge");
//            md.updateUser(user1, user1.getId());
        } else {
            System.out.println("Пользователь не найден");
        }
        db.release();
        connect.close();
    }
}