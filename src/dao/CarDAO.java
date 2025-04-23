package dao;
import entity.Car;
import java.sql.*;

public class CarDAO {
    private final Connection connection;

    public CarDAO(Connection connection) {
        this.connection = connection;
    }

    public void addCar(Car car) {
        String sql = "INSERT INTO cars (model, license_plate, mileage, fuel_level, last_maintenance_date, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, car.getModel());
            stmt.setString(2, car.getLicensePlate());
            stmt.setInt(3, car.getMileage());
            stmt.setDouble(4, car.getFuelLevel());
            stmt.setDate(5, Date.valueOf(car.getLastMaintenanceDate()));
            stmt.setBoolean(6, car.isAvailable());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Car getCarById(int car_id) {
        String sql = "SELECT * FROM cars WHERE car_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Car(
                        car_id,
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        rs.getInt("mileage"),
                        rs.getInt("fuel_level"),
                        rs.getString("last_maintenance_date"),
                        rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Car getCarByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM cars WHERE license_plate = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Car(
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        rs.getInt("mileage"),
                        rs.getInt("fuel_level"),
                        rs.getString("last_maintenance_date"),
                        rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateCar(Car car, int car_id) {
        String sql = "UPDATE cars SET model = ?, license_plate = ?, mileage = ?, fuel_level = ?, last_maintenance_date = ?, is_available = ? WHERE car_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, car.getModel());
            stmt.setString(2, car.getLicensePlate());
            stmt.setInt(3, car.getMileage());
            stmt.setDouble(4, car.getFuelLevel());
            stmt.setDate(5, Date.valueOf(car.getLastMaintenanceDate()));
            stmt.setBoolean(6, car.isAvailable());
            stmt.setInt(7, car_id);
            if (stmt.executeUpdate() > 0)
                System.out.println("Машина была успешно обновлена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCar(int car_id) throws SQLException {
        String sql = "DELETE FROM cars WHERE car_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car_id);
            if (stmt.executeUpdate() > 0)
                System.out.println("Машина была успешно удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
