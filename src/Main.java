import entity.User;
import types.UserRole;

public class Main {
    public static void main(String[] args) {
        User user1 = new User("admin", "ven741", UserRole.USER);
        System.out.println(user1.getRole());
    }
}