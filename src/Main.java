import entity.User;

public class Main {
    public static void main(String[] args) {
        User user1 = new User("admin", "ven741");
        System.out.println(user1.getId());
    }
}