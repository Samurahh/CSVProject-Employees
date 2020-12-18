package com.spartaglobal.samurah;

import com.spartaglobal.samurah.controller.EmployeeDAO;
import com.spartaglobal.samurah.controller.EmployeeManager;
import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import com.spartaglobal.samurah.model.DatabaseConnection;
import com.spartaglobal.samurah.model.EmployeeDTO;
import com.spartaglobal.samurah.model.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */


    @Test
    public void importingEmployees() throws IOException {
            EmployeeManager.importEmployees("EmployeeRecords.csv");
        Assertions.assertNotEquals(EmployeeRepository.employees.size(), 0);
    }

    @Test
    public void doNotLoseData(){
        try {
            importingEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long entries = EmployeeRepository.employees.size() + EmployeeRepository.invalidEmployees.size() + EmployeeRepository.corruptedEntries.size();
        Assertions.assertEquals(entries, 10000);

    }

    @Test
    public void shouldNotThrow(){
        boolean throwed = false;
        try {
            importingEmployees();
        } catch (IOException e) {
            throwed = true;
            e.printStackTrace();
        }

        Assertions.assertFalse(throwed);
    }

    @Test
    void checkConnection(){
        String url = EmployeeDAO.properties.getProperty("url");
        String user = EmployeeDAO.properties.getProperty("username");
        String password = EmployeeDAO.properties.getProperty("password");
        boolean works = true;
        try {
            Connection connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            works=false;
            e.printStackTrace();
        }
        Assertions.assertTrue(works);
    }

    @Test
    void validatingShouldWork(){
        try {
            importingEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EmployeeManager.checkAndValidateForTransfer();
        boolean valid = true;
        for (EmployeeDTO employee : EmployeeRepository.dbEmployees.values()) {
            if(EmployeeRepository.employees.containsKey(employee.getEmp_ID())){
                valid = false;
                break;
            }
        }
        Assertions.assertTrue(valid);
    }

    @Test
    void testingSavePacketsIfWithinSize(){
        try {
            importingEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLPacketGenerator checkingSave = new SQLPacketGenerator(EmployeeRepository.employees.values());
        checkingSave.formatSave();
        boolean ok = true;
        while(checkingSave.hasNext()){
            if(checkingSave.getNext().length() > SQLPacketGenerator.MAX_PACKET_SIZE){
                ok = false;
                break;
            }
        }
        Assertions.assertTrue(ok);
    }

    @Test
    void testingDeletePacketsIfWithinSize(){
        try {
            importingEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLPacketGenerator checkingDelete = new SQLPacketGenerator(EmployeeRepository.employees.values());
        checkingDelete.formatDelete();
        boolean ok = true;
        while(checkingDelete.hasNext()){
            if(checkingDelete.getNext().length() > SQLPacketGenerator.MAX_PACKET_SIZE){
                ok = false;
                break;
            }
        }
        Assertions.assertTrue(ok);
    }

}
