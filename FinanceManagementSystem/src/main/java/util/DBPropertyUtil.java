package util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    private static final String propertyFile = "src/main/java/config/db.properties";  // Ensure this path is correct

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(propertyFile)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getConnectionString() {
        Properties properties = loadProperties();
        String connectionString = properties.getProperty("db.url") + "?user=" + properties.getProperty("db.username") + "&password=" + properties.getProperty("db.password");
        return connectionString;
    }
}
