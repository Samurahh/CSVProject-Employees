package com.spartaglobal.samurah.model;

import com.spartaglobal.samurah.controller.CSVReader;
import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);
    static {
        logger.trace(DatabaseConnection.class + " initialized.");
    }
    static {
        logger.trace(DatabaseConnection.class + " establishing connection to database.");
    }

    private Connection connection;

    public DatabaseConnection(String url, String user, String password){
        try {
            connection = DriverManager.getConnection(url, user, password);
            logger.debug(Thread.currentThread().getName() + " successfully created a connection to database.");
        } catch (SQLException e) {
            logger.error(Thread.currentThread().getName() + " failed to connect to database.");
            System.err.printf("%s failed to connect to database.%n",Thread.currentThread().getName());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
