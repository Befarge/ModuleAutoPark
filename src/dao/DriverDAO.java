package dao;
import entity.Driver;
import java.sql.*;

public class DriverDAO {
    private final Connection connection;

    public DriverDAO(Connection connection) {
        this.connection = connection;
    }

    public void addDriver(Driver driver) {
        String sql = "INSERT INTO drivers (last_name, first_name, middle_name, age, phone_number, on_trip, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, driver.getLastName());
            stmt.setString(2, driver.getFirstName());
            stmt.setString(3, driver.getMiddleName());
            stmt.setInt(4, driver.getAge());
            stmt.setString(5, driver.getPhoneNumber());
            stmt.setBoolean(6, driver.isOnTrip());
            stmt.setInt(7, driver.getUserId());
            if (stmt.executeUpdate() > 0)
                System.out.println("Добавление прошло успешно");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Driver getDriverById(int driver_id) {
        String sql = "SELECT * FROM drivers WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driver_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Driver(
                        driver_id,
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("phone_number"),
                        rs.getBoolean("on_trip")
                );
            } else {
                System.out.println("Водитель не был найден");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Driver getDriverByUserId (int user_id) {
        String sql = "SELECT * FROM drivers WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Driver(
                        rs.getInt("driver_id"),
                        user_id,
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("phone_number"),
                        rs.getBoolean("on_trip")
                );
            } else {
                System.out.println("Водитель не был найден");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Driver getDriverByPhoneNumber (String phoneNumber) {
        String sql = "SELECT * FROM drivers WHERE phone_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Driver(
                        rs.getInt("driver_id"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        phoneNumber,
                        rs.getBoolean("on_trip")
                );
            } else {
                System.out.println("Водитель не был найден");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDriver(Driver driver) {
        String sql = """
                UPDATE drivers SET
                last_name = ?,
                first_name = ?,
                middle_name = ?,
                age = ?,
                phone_number = ?,
                on_trip = ?,
                user_id = ?
                WHERE driver_id = ?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, driver.getLastName());
            stmt.setString(2, driver.getFirstName());
            stmt.setString(3, driver.getMiddleName());
            stmt.setInt(4, driver.getAge());
            stmt.setString(5, driver.getPhoneNumber());
            stmt.setBoolean(6, driver.isOnTrip());
            stmt.setInt(7, driver.getUserId());
            stmt.setInt(8, driver.getId());
            if (stmt.executeUpdate() > 0)
                System.out.println("Обновление прошло успешно");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(int driver_id) {
        String sql = "DELETE FROM drivers WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driver_id);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Удаление прошло успешно");
            } else {
                System.out.println("Ничего не было удалено");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

