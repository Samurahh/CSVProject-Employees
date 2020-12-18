package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.Starter;
import com.spartaglobal.samurah.controller.CSVExporter;
import com.spartaglobal.samurah.controller.CommandInterpreter;
import com.spartaglobal.samurah.controller.EmployeeDAO;
import com.spartaglobal.samurah.controller.EmployeeManager;
import com.spartaglobal.samurah.model.Command;
import com.spartaglobal.samurah.model.EmployeeRepository;
import com.spartaglobal.samurah.view.ConsoleUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleCore {

    private static final         Logger logger = LogManager.getLogger(ConsoleCore.class);
    private static final LinkedList<String> inputHistory = new LinkedList<>(); //Up,Down Arrow history select TODO(NOT WORKING YET);
    private static final HashMap<String, Command> commands = new HashMap<>();
    /***
     * Console CORE - SINGLE THREAD
     */

    private static String currentInput = "";
    private static boolean running;

    static {
        logger.trace(ConsoleCore.class + " initialized.");
        running = true;
        initializeCommands();
    }

    private static void initializeCommands() {
        logger.trace("Commands initialized.");
        for (Command command : Command.values()) {
            commands.put(command.getCommand(), command);
        }
    }


    public static void start() {
        logger.debug("PROGRAM SUCCESSFULLY STARTED. \nWaiting for user interaction.");
        ConsoleUI.printHelper();
        do {
            currentInput = expectInput();
            processInputAndCallCommand(currentInput);
        } while (running);
    }

    public static void stop() {
        logger.debug("Stopping execution...");
        running = false;
    }

    private static void processInputAndCallCommand(String currentInput) {
        logger.debug("Processing and calling the command. Input: "+currentInput);
        String[] splitInput = currentInput.trim().split("\\s"); //removes the leading and trailing spaces, splits the input by whitespace chars
        Command command = commands.get(splitInput[0].toLowerCase(Locale.ROOT));
        String[] args = null;
        if (splitInput.length > 1) {
            args = currentInput.trim().substring(currentInput.indexOf(" ")).trim().split("\\s");
        }
        CommandInterpreter.execute(command, args);
    }

    public static void tryImport(String arg) {
        logger.debug("Trying to import file: "+arg);
        try {
            long initialTime = System.nanoTime();
            EmployeeManager.importEmployees(arg);
            long finalTime = System.nanoTime();
            checkStatus();
            ConsoleUI.printMessage("File " + arg + " successfully imported in " + (finalTime - initialTime) / 1_000_000 + " ms.");
        } catch (IOException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    public static void validate() {
        logger.debug("User requested a validation of imported entries.");
        if (EmployeeRepository.fileImported) {
            ConsoleUI.printMessage("Validating data imported against the database.");
            long initialTime = System.nanoTime();
            EmployeeManager.checkAndValidateForTransfer();
            long finalTime = System.nanoTime();
            ConsoleUI.printMessage("Entries successfully validated in " + (finalTime - initialTime) / 1_000_000 + " ms.");
        } else {
            ConsoleUI.printMessage("You need to import a file first!");
        }
    }

    public static void checkStatus() {
        ConsoleUI.printMessage(String.format("%d valid entries - ready to push to database", EmployeeRepository.employees.size()));
        ConsoleUI.printMessage(String.format("%d invalid entries - ready for export to file", EmployeeRepository.invalidEmployees.size()));
        ConsoleUI.printMessage(String.format("%d corrupt entries - ready for export to file", EmployeeRepository.corruptedEntries.size()));
    }

    public static void pushToDB() {
        if (EmployeeRepository.employees.size() == 0) {
            ConsoleUI.printError("No entries to be pushed to database!");
        } else {
            if (EmployeeRepository.validated) {
                EmployeeDAO.saveAll(EmployeeRepository.employees.values());
            } else {
                ConsoleUI.printError("The entries are not validated yet! Type 'validate' to validate.");
            }
        }
    }

    public static void stopExecution() {
        logger.debug("USER STOPPED THE PROGRAM");
        stop();
    }

    private static void addToHistory(String input) {
        if (inputHistory.size() > 10) {
            inputHistory.removeLast();
        }
        inputHistory.addFirst(input);
    }//Keeps the history of last 10 entries

    private static String expectInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            input = scanner.nextLine();
        } while (input.isBlank());
        addToHistory(currentInput);
        return input;
    } // Asks for input until the input is not blank, adds it to the history and returns the input.

    public static HashMap<String, Command> getCommands() {
        return commands;
    }

    public static void exportToFileInvalid(String arg) {
        if (EmployeeRepository.invalidEmployees.size() != 0) {
            try {
                CSVExporter.exportInvalid(arg, EmployeeRepository.invalidEmployees);
            } catch (IOException e) {
                System.err.println("An error occurred.");
            }
        } else{
            System.out.println("No invalid entries to export!");
        }
    }

    public static void exportToFileCorrupt(String arg) {
        if(EmployeeRepository.corruptedEntries.size() != 0) {
            try {
                CSVExporter.exportCorrupt(arg, EmployeeRepository.corruptedEntries);
            } catch (IOException e) {
                System.err.println("An error occurred.");
            }
        }else{
            System.out.println("No corrupt data to export!");
        }

    }
}
