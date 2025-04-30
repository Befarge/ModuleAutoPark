package types;
import db.DatabaseConnection;
import entity.Car;
import entity.Driver;
import entity.User;

public class SessionManager {
    private static User user;
    private static DatabaseConnection db;

    public static void setUser(User user, DatabaseConnection db) {
        SessionManager.user = user;
        SessionManager.db = db;
    }

    public static User getUser() {
        return user;
    }

    public static Driver getDriver() {
        return db.getDriverDAO().getDriverByUserId(user.getId());
    }

    public static boolean checkBlocked() {
        boolean isBlocked = false;
        if (db.getUserDAO().getUserById(user.getId()).getStatus() == UserStatus.BLOCKED)
            isBlocked = true;
        return isBlocked;
    }

    public static boolean checkOnTrip() {
        boolean isOnTrip = false;
        if (db.getDriverDAO().getDriverByUserId(user.getId()).isOnTrip())
            isOnTrip = true;
        return isOnTrip;
    }

    public static User getCopyUser() {
        return new User(user);
    }

    public static Car getCar() {
        return db.getCarDAO().getCarById(
                    db.getTripDAO().getTripByDriverId(
                            db.getDriverDAO().getDriverByUserId(
                                    user.getId()
                            ).getId()
                    ).getCarId()
        );
    }
}
