package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.model.EmployeeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeeBuilder{

    static{
        Logger logger = LogManager.getLogger(EmployeeBuilder.class);
        logger.trace(EmployeeBuilder.class + " initialized.");
    }


    private static EmployeeDTO createEmployee(String input, Boolean existsInDB){
        String[] fields = input.split(",");
        EmployeeDTO newEmployee = new EmployeeDTO(existsInDB);
        newEmployee.setEmp_ID(fields[0]);
        newEmployee.setNamePrefix(fields[1]);
        newEmployee.setFirstName(fields[2]);
        newEmployee.setMiddleInitial(fields[3]);
        newEmployee.setLastName(fields[4]);
        newEmployee.setGender(fields[5]);
        newEmployee.setEmail(fields[6]);
        newEmployee.setDateOfBirth(fields[7]);
        newEmployee.setDateOfJoining(fields[8]);
        newEmployee.setSalary(fields[9]);
        return  newEmployee;
    }

    public static EmployeeDTO importEmployee(String input){
        return createEmployee(input, false);
    }

    public static EmployeeDTO fetchEmployee(String input){
        return createEmployee(input, true);
    }
}
