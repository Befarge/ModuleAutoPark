import entity.User;
import types.UserRole;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(2,"admin", "ven741", UserRole.ADMIN);
        user1.printInfo();
    }
}