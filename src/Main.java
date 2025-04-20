import entity.User;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(1,"admin", "ven741");
        System.out.println(user1.getId());
        user1.setId(2);
        System.out.println(user1.getId());
        System.out.println(user1.getPassword());
    }
}