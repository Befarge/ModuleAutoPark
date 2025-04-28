package dao;
import customException.*;
import entity.Trip;
import java.sql.*;

public class TripDAO {
    private final Connection connection;

    public TripDAO(Connection connection) {
        this.connection = connection;
    }

    public void addTrip(Trip trip) throws SQLException {
        String sql = """
                INSERT INTO trips
                (driver_id, car_id, start_time, end_time, distance, fuel_used)
                VALUES (?, ?, NOW(), ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trip.getDriverId());
            stmt.setInt(2, trip.getCarId());
            stmt.setObject(3, trip.getEndTime(), Types.TIMESTAMP);
            stmt.setObject(4, trip.getDistance(), Types.INTEGER);
            stmt.setObject(5, trip.getFuelUsed(), Types.INTEGER);
            System.out.println(stmt.executeUpdate());
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getSQLState().equals("23514")) {
                String msg = ex.getMessage();
                if (msg.contains("check_distance"))
                    throw new CheckDistanceException("Расстояние от 0.");
                else if (msg.contains("check_fuel_used"))
                    throw new CheckFuelException("Топливо от 0.");
                else
                    throw new CheckException("Не прошли чеки.");
            } else {
                throw ex;
            }
        }
    }

    public void finishTrip(int distance, int fuel, int trip_id) throws SQLException {
        String sql = "UPDATE trips SET end_time = NOW(), distance = ?, fuel_used = ? WHERE trip_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, distance);
            stmt.setInt(2, fuel);
            stmt.setInt(3, trip_id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Поездка успешно завершена.");
            } else {
                System.out.println("Поездка с указанным ID не найдена.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getSQLState().equals("23514")) {
                String msg = ex.getMessage();
                if (msg.contains("check_distance"))
                    throw new CheckDistanceException("Расстояние от 0.");
                else if (msg.contains("check_fuel_used"))
                    throw new CheckFuelException("Топливо от 0.");
                else
                    throw new CheckException("Не прошли чеки.");
            } else {
                throw ex;
            }
        }
    }


    public void updateTrip(Trip trip) {
        String sql = """
                        UPDATE trips
                        SET driver_id = ?, car_id = ?, start_time = ?, end_time = ?, distance = ?, fuel_used = ?
                        WHERE trip_id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trip.getDriverId());
            stmt.setInt(2, trip.getCarId());
            stmt.setTimestamp(3, Timestamp.valueOf(trip.getStartTime()));
            if (trip.getEndTime() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(trip.getEndTime()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }
            stmt.setDouble(5, trip.getDistance());
            stmt.setDouble(6, trip.getFuelUsed());
            stmt.setInt(7, trip.getId());

            if (stmt.executeUpdate() > 0)
                System.out.println("Поездка успешно обновлена.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Получить поездку по ID
    public Trip getTripById(int trip_id) {
        String sql = "SELECT * FROM trips WHERE trip_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trip_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Trip(
                        rs.getInt("trip_id"),
                        rs.getInt("driver_id"),
                        rs.getInt("car_id"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("distance"),
                        rs.getInt("fuel_used")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Trip getTripByDriverId(int driver_id) {
        String sql = "SELECT * FROM trips WHERE driver_id = ? AND end_time IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driver_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Trip(
                        rs.getInt("trip_id"),
                        driver_id,
                        rs.getInt("car_id"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("distance"),
                        rs.getInt("fuel_used")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Trip getTripByCarId(int car_id) {
        String sql = "SELECT * FROM trips WHERE car_id = ? AND end_time IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Trip(
                        rs.getInt("trip_id"),
                        rs.getInt("driver_id"),
                        car_id,
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("distance"),
                        rs.getInt("fuel_used")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTrip(int trip_id) throws SQLException {
        String sql = "DELETE FROM trips WHERE trip_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trip_id);
            if (stmt.executeUpdate() > 0)
                System.out.println("Удаление прошло успешно");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTripByCar(int car_id) throws SQLException {
        String sql = "DELETE FROM trips WHERE car_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car_id);
            if (stmt.executeUpdate() > 0)
                System.out.println("Удаление прошло успешно");
        }
    }
}
