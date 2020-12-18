package com.spartaglobal.samurah.model;

import com.spartaglobal.samurah.controller.CSVReader;
import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeRepository {
    private static final Logger logger = LogManager.getLogger(SQLPacketGenerator.class);
    static {
        logger.trace(CSVReader.class + " initialized.");
    }
    public static ArrayList<String> corruptedEntries = new ArrayList<>(); //rows with missing columns
    public static HashMap<String, EmployeeDTO> employees = new HashMap<>(); //valid employees, ready to push to db
    public static ArrayList<EmployeeDTO> invalidEmployees = new ArrayList<>(); //duplicates or empty column entries
    public static HashMap<String, EmployeeDTO> dbEmployees = new HashMap<>(); //employees already existing in db
    public static String csvHeader = ""; //the csvHeader of the imported file
    public static boolean fileImported = false;
    public static boolean validated = false;

    public static void markAsValidated() {
        logger.debug("Employee entries marked as validated!");
        validated = true;
    }

    public static void resetAll() {
        logger.debug("RESET REPOSITORY");
        employees.clear();
        csvHeader = "";
        invalidEmployees.clear();
        dbEmployees.clear();
        corruptedEntries.clear();
        fileImported = false;
        validated = false;
    }
}
