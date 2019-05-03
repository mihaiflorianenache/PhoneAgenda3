package org.fasttrackit.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfiguration {
    public static Connection getConnection()  {
        try {
            Properties properties = new Properties();
            InputStream inputStream = DatabaseConfiguration.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(inputStream);
            Class.forName(properties.getProperty("DB_DRIVER_CLASS"));
            return DriverManager.getConnection(properties.getProperty("DB_URL"), properties.getProperty("DB_USERNAME"), properties.getProperty("DB_PASSWORD"));
        }catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to connect to the db.");
    }
}

