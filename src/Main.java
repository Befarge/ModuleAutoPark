import entity.Car;

public class Main {
    public static void main(String[] args) {
        Car car1 = new Car(
                "Nissan Almera",
                "О170СМ",
                1000,
                50,
                "10-03-25"
        );

        car1.printInfo();
    }
}