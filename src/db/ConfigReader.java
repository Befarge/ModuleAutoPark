package db;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private final Properties properties;

    public ConfigReader(String filePath) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new IOException("Файл config.properties не найден в ресурсах");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка с конфигурационным файлом");
        }
    }

    public String getUrl() {
        return properties.getProperty("db.url");
    }

    public String getUsername() {
        return properties.getProperty("db.username");
    }

    public String getPassword() {
        return properties.getProperty("db.password");
    }
}

