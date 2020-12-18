package com.spartaglobal.samurah.controller;


import com.spartaglobal.samurah.model.EmployeeDTO;
import com.spartaglobal.samurah.model.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class EmployeeManager {

    private static final Logger logger = LogManager.getLogger(EmployeeManager.class);

    static {
        logger.trace(EmployeeManager.class + " initialized.");
    }


    public static void importEmployees(String filename) throws IOException {
        EmployeeRepository.resetAll();
        String folderPath = "src/main/resources/";
        CSVReader csvReader = new CSVReader(folderPath + filename);
        LinkedList<EmployeeDTO> importedEmployees = csvReader.getEmployees();
        while (importedEmployees.peekFirst() != null) {
            EmployeeDTO employee = importedEmployees.poll();
            if (employee.isValid()) {
                if (EmployeeRepository.employees.containsKey(employee.getEmp_ID())) {
                    EmployeeRepository.invalidEmployees.add(employee);
                    EmployeeRepository.invalidEmployees.add(EmployeeRepository.employees.get(employee.getEmp_ID()));
                    EmployeeRepository.employees.remove(employee.getEmp_ID());
                } else {
                    EmployeeRepository.employees.put(employee.getEmp_ID(), employee);
                }
            } else {
                EmployeeRepository.invalidEmployees.add(employee);
            }
        }
        EmployeeRepository.csvHeader = csvReader.getHeader();
        EmployeeRepository.fileImported = true;
    }

    public static void fetchEmployeesFromDB() {
        logger.debug("Fetching employees from database.");
        EmployeeRepository.dbEmployees = EmployeeDAO.getAll();
    }

    public static void checkAndValidateForTransfer() {
        fetchEmployeesFromDB();
        logger.debug("Checking and validating entries for transfer...");
        System.out.println("Checking and validating entries for transfer...");
        int duplicatesWithDB = 0;
        int valid = 0;
        int identical = 0;
        ArrayList<String> duplicatesToRemove = new ArrayList<>();
        HashMap<String, EmployeeDTO> firstRepository;
        HashMap<String, EmployeeDTO> secondRepository;

        if (EmployeeRepository.dbEmployees.size() > EmployeeRepository.employees.size()) {
            firstRepository = EmployeeRepository.employees;
            secondRepository = EmployeeRepository.dbEmployees;
        } else {
            firstRepository = EmployeeRepository.dbEmployees;
            secondRepository = EmployeeRepository.employees;
        }
        for (EmployeeDTO employee : firstRepository.values()) {
            if (secondRepository.containsKey(employee.getEmp_ID())) {
                if (employee.isIdentical(secondRepository.get(employee.getEmp_ID()))) {
                    duplicatesToRemove.add(employee.getEmp_ID());
                    identical++;
                } else {
                    EmployeeRepository.invalidEmployees.add(employee);
                    duplicatesToRemove.add(employee.getEmp_ID());
                    duplicatesWithDB++;
                }
            } else {
                valid++;
            }
        }
        for (String s : duplicatesToRemove) {
            EmployeeRepository.employees.remove(s);
        }
        EmployeeRepository.markAsValidated();
        if (duplicatesWithDB > 0) {
            System.out.printf("Found %s entries matching with existing IDs in database. Entries moved to invalid entries.%n", duplicatesWithDB);
        } else {
            System.out.println("No entries match with existing IDs in database. OK");
        }
        if (identical > 0) {
            System.out.printf("Found %d identical entries already existing in database. Removed for import.%n", identical);
        } else {
            System.out.println("No identical entries found in database. OK");
        }
        if (valid > 0 && EmployeeRepository.dbEmployees.size() > 0) {
            System.out.printf("%d valid entries ready for transfer.%n", valid);
        } else if (EmployeeRepository.dbEmployees.size() == 0) {
            System.out.printf("%d valid entries ready for transfer.%n", EmployeeRepository.employees.size());
        } else {
            System.out.println("No valid entries for transfer !!");
        }
    }

    public static void printEmployees() {
        for (EmployeeDTO employee : EmployeeRepository.employees.values()) {
            System.out.println(employee.toString());
        }
        System.out.println("Size " + EmployeeRepository.employees.size());
    }

    public static void printInvalidEmployees() {
        for (EmployeeDTO employee : EmployeeRepository.invalidEmployees) {
            System.out.println(employee.toString());
        }
        System.out.println("Size " + EmployeeRepository.invalidEmployees.size());
    }
}
