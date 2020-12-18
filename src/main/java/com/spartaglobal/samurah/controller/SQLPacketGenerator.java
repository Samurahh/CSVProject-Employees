package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.model.EmployeeDTO;
import com.spartaglobal.samurah.model.EmployeeFields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class SQLPacketGenerator {

    private static final Logger logger = LogManager.getLogger(SQLPacketGenerator.class);

    static {
        logger.trace(SQLPacketGenerator.class + " initialized.");
    }
    {
        logger.trace(SQLPacketGenerator.class + "has been instantiated.");
    }

    public static final int MAX_PACKET_SIZE = 524288; // 512KB MAX PACKET SIZE 524288
    private final Collection<EmployeeDTO> employees;
    private boolean formatted = false;
    private String formatType = null;
    private final LinkedList<String> queue = new LinkedList<>();


    public SQLPacketGenerator(Collection<EmployeeDTO> input){
        this.employees = input;
    }

    public SQLPacketGenerator formatSave(){
        if(!formatted) {
            formatted = true;
            formatType = "SAVE";
            String header = String.format("INSERT INTO %s.%s (`emp_id`, `name_prefix`, `first_name`, `middle_initial`," +
                            " `last_name`, `gender`, `email`, `date_of_birth`, `date_of_joining`, `salary`) VALUES ",
                    EmployeeDAO.properties.getProperty("databaseScheme"), EmployeeDAO.TABLE_NAME);

            StringBuilder currentStep = new StringBuilder(header);
            long packetsSize = 0;
            int numberOfPackets = 0;
            for (EmployeeDTO employee : employees) {
                if ((employee.getSQLValue().length() + currentStep.length()) >= MAX_PACKET_SIZE) {
                    currentStep.deleteCharAt(currentStep.lastIndexOf(","));
                    packetsSize+=currentStep.length();
                    numberOfPackets++;
                    queue.add(currentStep.toString());
                    currentStep = new StringBuilder(header);
                }
                currentStep.append(employee.getSQLValue());
                currentStep.append(", \n");
            }
            if (currentStep.length() > header.length()) {
                currentStep.deleteCharAt(currentStep.lastIndexOf(","));
                numberOfPackets++;
                packetsSize+=currentStep.length();
                queue.add(currentStep.toString());
            }
            System.out.printf("Generated %d packets of total size: %d KB%n", numberOfPackets,packetsSize/1024);

        }
        return this;
    }

    public Iterator<String> getIterator(){
        return queue.iterator();
    }

    public SQLPacketGenerator formatDelete(){
        if(!formatted) {
            formatted = true;
            formatType = "DELETE";
            String header = String.format("DELETE FROM %s.%s WHERE %s IN (", EmployeeDAO.properties.getProperty("databaseScheme"), EmployeeDAO.TABLE_NAME,
                    EmployeeFields.EMP_ID);
            StringBuilder currentStep = new StringBuilder(header);
            long packetsSize = 0;
            int numberOfPackets = 0;
            for (EmployeeDTO employee : employees) {
                String internalString = employee.getEmp_ID() + ",";
                if ((internalString.length() + currentStep.length()) >= MAX_PACKET_SIZE) {
                    currentStep.deleteCharAt(currentStep.lastIndexOf(","));
                    currentStep.append(")");
                    packetsSize+=currentStep.length();
                    numberOfPackets++;
                    queue.add(currentStep.toString());
                    currentStep = new StringBuilder(header);
                }
                currentStep.append(internalString);
            }
            if (currentStep.length() > header.length()) {
                currentStep.deleteCharAt(currentStep.lastIndexOf(","));
                currentStep.append(")");
                numberOfPackets++;
                packetsSize+=currentStep.length();
                queue.add(currentStep.toString());
            }
            System.out.printf("Generated %d packets of total size: %d KB%n", numberOfPackets,packetsSize/1024);
        }
        return this;
    }

    public String getFormatType(){
        return formatType;
    }

    public synchronized String getNext(){
        return queue.pollFirst();
    }

    public synchronized boolean hasNext(){
        return queue.peek() != null;
    }

}
