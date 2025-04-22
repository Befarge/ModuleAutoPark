package entity;
import types.UserRole;

public class User {
    // Поля для пользователя
    private Integer id;
    private String login;
    private String password;
    private UserRole role;

    // Конструктор
    public User(int id, String login, String password, UserRole role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) { this.role = role; }

    public void printInfo() {
        System.out.printf("id = %d, login = %s, password = %s, role = %s", id, login, password, role.toString());
    }
}
