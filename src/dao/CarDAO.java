package dao;
import customException.*;
import entity.Car;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private final Connection connection;

    public CarDAO(Connection connection) {
        this.connection = connection;
    }

    public void addCar(Car car) throws SQLException {
        String sql = "INSERT INTO cars (model, license_plate, mileage, fuel_level, last_maintenance_date, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, car.getModel());
            stmt.setString(2, car.getLicensePlate());
            stmt.setInt(3, car.getMileage());
            stmt.setDouble(4, car.getFuelLevel());
            stmt.setString(5, car.getLastMaintenanceDate());
            stmt.setBoolean(6, car.isAvailable());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getSQLState().equals("23514")) {
                String msg = ex.getMessage();
                if (msg.contains("check_distance"))
                    throw new CheckDistanceException("Расстояние от 0.");
                else if (msg.contains("check_fuel_used"))
                    throw new CheckFuelException("Топливо от 0.");
                else if (msg.contains("check_date"))
                    throw new CheckDateException("Неправильный формат даты.");
                else if (msg.contains("check_license_plate"))
                    throw new CheckLicensePlateException("Неправильный формат номера.");
                else
                    throw new CheckException("Не прошли чеки.");
            } else if (ex.getSQLState().equals("23505")) {
                String msg = ex.getMessage();
                if (msg.contains("car_license_plate_key"))
                    throw new UniquenessLicensePlateException("Номер такой машины уже существует.");
                else
                    throw new UniquenessException("Ошибка уникальности.");
            } else {
                throw ex;
            }
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


    public void updateCar(Car car) throws SQLException {
        String sql = "UPDATE cars SET model = ?, license_plate = ?, mileage = ?, fuel_level = ?, last_maintenance_date = ?, is_available = ? WHERE car_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, car.getModel());
            stmt.setString(2, car.getLicensePlate());
            stmt.setInt(3, car.getMileage());
            stmt.setDouble(4, car.getFuelLevel());
            stmt.setString(5, car.getLastMaintenanceDate());
            stmt.setBoolean(6, car.isAvailable());
            stmt.setInt(7, car.getId());
            if (stmt.executeUpdate() > 0)
                System.out.println("Машина была успешно обновлена");
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getSQLState().equals("23514")) {
                String msg = ex.getMessage();
                if (msg.contains("check_distance"))
                    throw new CheckDistanceException("Расстояние от 0.");
                else if (msg.contains("check_fuel_used"))
                    throw new CheckFuelException("Топливо от 0.");
                else if (msg.contains("check_date"))
                    throw new CheckDateException("Неправильный формат даты.");
                else if (msg.contains("check_license_plate"))
                    throw new CheckLicensePlateException("Неправильный формат номера.");
                else
                    throw new CheckException("Не прошли чеки.");
            } else if (ex.getSQLState().equals("23505")) {
                String msg = ex.getMessage();
                if (msg.contains("car_license_plate_key"))
                    throw new UniquenessLicensePlateException("Номер такой машины уже существует.");
                else
                    throw new UniquenessException("Ошибка уникальности.");
            } else {
                throw ex;
            }
        }
    }

    public void deleteCar(int car_id) throws SQLException {
        String sql = "DELETE FROM cars WHERE car_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car_id);
            if (stmt.executeUpdate() > 0)
                System.out.println("Машина была успешно удалена");
            else
                throw new InactionException("Ничего не было удалено");
        }
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE is_available = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        rs.getInt("mileage"),
                        rs.getInt("fuel_level"),
                        rs.getString("last_maintenance_date"),
                        rs.getBoolean("is_available")
                );
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

}
