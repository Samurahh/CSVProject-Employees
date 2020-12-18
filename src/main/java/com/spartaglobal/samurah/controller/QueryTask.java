package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import com.spartaglobal.samurah.controller.EmployeeDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QueryTask implements Runnable {
    private static Logger logger = LogManager.getLogger(QueryTask.class);
    static{
        logger.trace(QueryTask.class + " initialized.");
    }

    {
        logger.trace(QueryTask.class + " has been instantiated.");
    }

    private final SQLPacketGenerator sqlPacketGenerator;
    private Connection connection;

    {
        String url = EmployeeDAO.properties.getProperty("url");
        String username = EmployeeDAO.properties.getProperty("username");
        String password = EmployeeDAO.properties.getProperty("password");
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.printf("[%s]Connected to the database.%n",Thread.currentThread().getName());
        } catch (SQLException e) {
            System.err.printf("[%s]Connection could not be established!%n %s%n",Thread.currentThread().getName(), e.getMessage());
        }
    }

    public QueryTask(SQLPacketGenerator sqlPacketGenerator) {
        this.sqlPacketGenerator = sqlPacketGenerator;
    }

    @Override
    public void run() {
        String formatType = sqlPacketGenerator.getFormatType();
        switch (formatType) {
            case "SAVE": {
                save();
                break;
            }
            case "DELETE": {
                delete();
                break;
            }
        }
    }

    private void save() {
        while (sqlPacketGenerator.hasNext()) {
            try {
                EmployeeDAO.savePacket(sqlPacketGenerator.getNext(),connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void delete() {

    }
}
