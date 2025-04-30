package entity;

public class Driver {
    private Integer driver_id;     // Идентификатор водителя
    private Integer user_id;       // Идентификатор юзера (внешний ключ)
    private String lastName;       // Фамилия
    private String firstName;      // Имя
    private String middleName;     // Отчество
    private int age;               // Возраст
    private String phoneNumber;    // Номер телефона
    private boolean onTrip;        // В поездке или нет

    public Driver(int driver_id, int user_id, String firstName, String middleName, String lastName, int age, String phoneNumber, boolean onTrip) {
        this.driver_id = driver_id;
        this.user_id = user_id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.onTrip = onTrip;
    }

    public Driver(String firstName, String middleName, String lastName, int age, String phoneNumber, int user_id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.user_id = user_id;
    }

    // Геттеры и сеттеры

    public Integer getId() {
        return driver_id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public Driver setUserId(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Driver setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Driver setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Driver setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Driver setAge(int age) {
        this.age = age;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Driver setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public boolean isOnTrip() {
        return onTrip;
    }

    public Driver setOnTrip(boolean onTrip) {
        this.onTrip = onTrip;
        return this;
    }

    @Override
    public String toString() {
        return "Имя: " + firstName + ", Номер: " + phoneNumber;
    }
}
