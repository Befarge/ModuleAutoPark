package entity;
import types.UserRole;
import types.UserStatus;

public class User {
    // Поля для пользователя
    private Integer id;
    private String login;
    private String password;
    private UserRole role;
    private UserStatus status;

    // Конструктор
    public User(int id, String login, String password, UserRole role, UserStatus status) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.status = UserStatus.WAIT;
    }

    public User (User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.status = user.getStatus();
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

    public UserStatus getStatus() { return status; }

    public void setStatus (UserStatus status) { this.status = status; }
}
