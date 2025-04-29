package entity;

public class Trip {
    private Integer trip_id;
    private int driver_id;
    private int car_id;
    private String startTime;
    private String endTime;
    private Integer distance; // в километрах
    private Integer fuelUsed; // в литрах

    // Конструктор для создания полной записи (например, при загрузке из БД)
    public Trip (
            int trip_id, int driver_id, int car_id,
            String startTime, String endTime, int distance, int fuelUsed
    ) {
        this.trip_id = trip_id;
        this.driver_id = driver_id;
        this.car_id = car_id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.fuelUsed = fuelUsed;
    }

    // Конструктор для начала поездки (например, перед тем как начать)
    public Trip (int driver_id, int car_id) {
        this.driver_id = driver_id;
        this.car_id = car_id;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return trip_id;
    }

    public int getDriverId() {
        return driver_id;
    }

    public void setDriverId(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getCarId() {
        return car_id;
    }

    public void setCarId(int car_id) {
        this.car_id = car_id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Integer getFuelUsed() {
        return fuelUsed;
    }

    public void setFuelUsed(int fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    @Override
    public String toString() {
        return "Начало: " + startTime.substring(0,16) + ", Конец: " + endTime.substring(0,16);
    }
}
