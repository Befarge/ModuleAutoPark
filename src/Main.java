import entity.Driver;

public class Main {
    public static void main(String[] args) {
        Driver driver1 = new Driver(
                "Евгений",
                "Александрович",
                "Веников",
                19,
                "89611722076",
                1
        );

        driver1.printInfo();
    }
}