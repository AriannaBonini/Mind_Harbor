package com.example.mindharbor.sessione;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);


    private static Connection connection;

    private ConnectionFactory() {}

    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            if (input == null){
                logger.error("Impossibile recuperare le credenziali del DB");
            }
            properties.load(input);

            String jdbcURL = properties.getProperty("jdbcURL");
            String jdbcUsername = properties.getProperty("jdbcUsername");
            String jdbcPassword = properties.getProperty("jdbcPassword");


            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

        } catch (IOException | SQLException e) {
            logger.error("Tentativo di connessione al database fallito", e);
        }
    }


    public static Connection getConnection() {
        if(connection==null) {
            System.out.println("nulla");
        }
        return connection;
    }
}
