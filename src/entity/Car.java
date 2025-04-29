package entity;

public class Car {
    private Integer car_id;
    private String model;
    private String licensePlate;
    private int mileage;
    private int fuelLevel;
    private String lastMaintenanceDate;
    private boolean isAvailable;

    public Car(int car_id, String model, String licensePlate, int mileage, int fuelLevel, String lastMaintenanceDate, boolean isAvailable) {
        this.car_id = car_id;
        this.model = model;
        this.licensePlate = licensePlate;
        this.mileage = mileage;
        this.fuelLevel = fuelLevel;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.isAvailable = isAvailable;
    }

    public Car(String model, String licensePlate, int mileage, int fuelLevel, String lastMaintenanceDate) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.mileage = mileage;
        this.fuelLevel = fuelLevel;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.isAvailable = true;
    }

    public Car(Car car) {
        this.car_id = car.getId();
        this.model = car.getModel();
        this.licensePlate = car.getLicensePlate();
        this.mileage = car.getMileage();
        this.fuelLevel = car.getFuelLevel();
        this.lastMaintenanceDate = car.getLastMaintenanceDate();
        this.isAvailable = car.isAvailable();
    }

    // Getters & Setters

    public int getId() {
        return car_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(String lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Модель: " + model + ", Номер: " + licensePlate;
    }
}
