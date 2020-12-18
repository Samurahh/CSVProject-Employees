package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.model.EmployeeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

// CONTROLLER - READING FILES

public class CSVReader {
    private static final Logger logger = LogManager.getLogger(CSVReader.class);

    static {
        logger.trace(CSVReader.class + " initialized.");
    }
    static {
        logger.trace(CSVReader.class + " has been instantiated.");
    }


    private final BufferedReader reader;
    private String header;

    public CSVReader(String path) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(path));
    }

    public LinkedList<EmployeeDTO> getEmployees() {
        LinkedList<EmployeeDTO> employees = new LinkedList<>();
        try {
            String input;
            this.header = reader.readLine();
            while ((input = reader.readLine()) != null) {
                employees.add(getEmployee(input));
            }
        } catch (IOException e) {
            System.err.println("Unexpected error while reading file.");
        }
        return employees;
    }

    private EmployeeDTO getEmployee(String input) {
        return EmployeeBuilder.importEmployee(input);
    }

    public String getHeader() {
        return header;
    }
}
