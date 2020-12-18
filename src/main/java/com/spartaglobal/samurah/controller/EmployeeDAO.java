package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.Starter;
import com.spartaglobal.samurah.model.BasicConnectionPool;
import com.spartaglobal.samurah.model.EmployeeDTO;
import com.spartaglobal.samurah.model.EmployeeFields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmployeeDAO {

    static{
        Logger logger = LogManager.getLogger(EmployeeDAO.class);
        logger.trace(EmployeeDAO.class + " initialized.");
    }


    public static final Properties properties = new Properties();
    public static final String TABLE_NAME = "employees";
    private static Connection connection = null;
    public static BasicConnectionPool connectionPool = null;

    static {
        createProperties();
        try {
            connectionPool = connectToDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = connectionPool.getConnection();
    }

    private static BasicConnectionPool connectToDB() throws SQLException {
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        return BasicConnectionPool.create(url,username,password);
    }

    private static void createProperties() {
        try {
            properties.load(new FileReader("src/main/resources/database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<EmployeeDTO> get(int id) {
        return Optional.ofNullable(select(String.format("SELECT * FROM %s.%s WHERE %s = %d", properties.getProperty("databaseScheme"), EmployeeDAO.TABLE_NAME,
                EmployeeFields.EMP_ID, id)).get(String.valueOf(id)));
    }

    public static HashMap<String, EmployeeDTO> getAll() {
        return select(String.format("SELECT * FROM %s.%s", properties.getProperty("databaseScheme"), EmployeeDAO.TABLE_NAME));
    }

    public static void save(EmployeeDTO employeeDTO) {
        saveAll(Collections.singletonList(employeeDTO));
    }

    public static void savePacket(String packet, Connection threadConnection) throws SQLException {
        threadConnection.createStatement().execute(packet);
    }

    public static void deletePacket(String packet, Connection threadConnection) throws SQLException {
        threadConnection.createStatement().execute(packet);
    }

    public static void saveAll(Collection<EmployeeDTO> employees) {
        long initial = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(Starter.THREADS);
        new SQLPacketGenerator(employees).formatSave().getIterator().forEachRemaining(it-> executorService.execute(()->{
            try {
                Connection threadConnection = connectionPool.getConnection();
                savePacket(it, threadConnection);
                connectionPool.releaseConnection(threadConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
        executorService.shutdown();
        long finalTime = System.nanoTime();
        System.out.printf("%d entries pushed to database successfully!%nTime taken: %d ms.%n", employees.size(),(finalTime-initial)/1_000_000);
    }

    public static void update(EmployeeDTO employeeDTO) {
        //TODO("NOT REQUIRED");
    }

    public static void delete(EmployeeDTO employeeDTO) {
        deleteAll(Collections.singletonList(employeeDTO));
    }

    public static void deleteAll(Collection<EmployeeDTO> employees) {
        long initial = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(Starter.THREADS);
        new SQLPacketGenerator(employees).formatDelete().getIterator().forEachRemaining(it-> executorService.execute(()->{
            try {
                Connection threadConnection = connectionPool.getConnection();
                deletePacket(it, threadConnection);
                connectionPool.releaseConnection(threadConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
        executorService.shutdown();
        long finalTime = System.nanoTime();
        System.out.printf("%d entries deleted from the database successfully!%nTime taken: %d ms.%n", employees.size(),(finalTime-initial)/1_000_000);
    }

    private static HashMap<String, EmployeeDTO> select(String query) {
        HashMap<String, EmployeeDTO> employeesQueried = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                StringBuilder input = new StringBuilder();
                String employeeStringCSV = CSVConverter.convertToCsvLine(resultSet,input);
                EmployeeDTO employee = EmployeeBuilder.fetchEmployee(employeeStringCSV);
                employeesQueried.put(employee.getEmp_ID(), employee);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return employeesQueried;
    }

    private static void convertToCsvLine(ResultSet resultSet, StringBuilder input) throws SQLException {
        input.append(resultSet.getInt(EmployeeFields.EMP_ID.getOrder() + 1)).append(",");
        input.append(resultSet.getString(EmployeeFields.NAME_PREFIX.getOrder() + 1)).append(",");
        input.append(resultSet.getString(EmployeeFields.FIRST_NAME.getOrder() + 1)).append(",");
        input.append(resultSet.getString(EmployeeFields.MIDDLE_INITIAL.getOrder() + 1)).append(",");
        input.append(resultSet.getString(EmployeeFields.LAST_NAME.getOrder() + 1)).append(",");
        input.append(resultSet.getString(EmployeeFields.E_MAIL.getOrder() + 1)).append(",");
        input.append(resultSet.getString(EmployeeFields.GENDER.getOrder() + 1)).append(",");
        input.append(resultSet.getDate(EmployeeFields.DATE_OF_BIRTH.getOrder() + 1)).append(",");
        input.append(resultSet.getDate(EmployeeFields.DATE_OF_JOINING.getOrder() + 1)).append(",");
        input.append(resultSet.getInt(EmployeeFields.SALARY.getOrder() + 1));
    }
}
