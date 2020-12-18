package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.Starter;
import com.spartaglobal.samurah.model.EmployeeDTO;
import com.spartaglobal.samurah.model.EmployeeFields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVConverter {

    static{
        Logger logger = LogManager.getLogger(CSVConverter.class);
        logger.trace(CSVConverter.class + " initialized.");
    }

    public static String convertToCsvLine(ResultSet resultSet, StringBuilder input) throws SQLException {
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
        return input.toString();
    }

    public static String convertToCsvLine(EmployeeDTO employeeDTO) {
        StringBuilder output = new StringBuilder();
        output.append(employeeDTO.getEmp_ID()).append(",");
        output.append(employeeDTO.getNamePrefix()).append(",");
        output.append(employeeDTO.getFirstName()).append(",");
        output.append(employeeDTO.getMiddleInitial()).append(",");
        output.append(employeeDTO.getLastName()).append(",");
        output.append(employeeDTO.getEmail()).append(",");
        output.append(employeeDTO.getGender()).append(",");
        output.append(employeeDTO.getDateOfBirth()).append(",");
        output.append(employeeDTO.getDateOfJoining()).append(",");
        output.append(employeeDTO.getSalary());
        return output.toString();
    }
}
