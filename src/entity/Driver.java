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

    public Driver(String firstName, String middleName, String lastName, int age, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    // Геттеры и сеттеры

    public Integer getId() {
        return driver_id;
    }

    public void setId(int driver_id) {
        this.driver_id = driver_id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOnTrip() {
        return onTrip;
    }

    public void setOnTrip(boolean onTrip) {
        this.onTrip = onTrip;
    }

    public void printInfo() {
        System.out.printf("""
              driver_id = %d
              user_id = %d
              firstName = %s
              middleName = %s
              lastName = %s
              age = %d
              phoneNumber = %s
              onTrip = %b
              
              """ ,
              driver_id, user_id, firstName, middleName, lastName, age, phoneNumber, onTrip
        );
    }
}
