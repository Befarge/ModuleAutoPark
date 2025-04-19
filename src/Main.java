import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        //Создаем объект Property для выборки данных из файла
        Properties properties = new Properties();

        // GUI: создаём окно
        JFrame frame = new JFrame("Данные из базы");
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            Connection conn = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password")
            );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    textArea.append(meta.getColumnName(i) + ": " + rs.getString(i) + "\n");
                }
                textArea.append("-----------\n");
            }
        } catch (SQLException e) {
            textArea.setText("Ошибка подключения к базе:\n" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            textArea.setText("Ошибка c конфигурационным файлом:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}