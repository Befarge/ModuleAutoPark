package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final int MAX_CONNECTIONS = 1;
    private static int currentConnections = 0;

    private final ConfigReader config;

    public DatabaseConnection(ConfigReader configReader) {
        this.config = configReader;
    }

    public Connection connect() {
        if (currentConnections >= MAX_CONNECTIONS) {
            System.out.println("Превышен лимит соединений: " + MAX_CONNECTIONS);
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
            currentConnections++;
            return connection;
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
            return null;
        }
    }

    public void release() {
        if (currentConnections > 0) {
            currentConnections--;
        }
    }
}

