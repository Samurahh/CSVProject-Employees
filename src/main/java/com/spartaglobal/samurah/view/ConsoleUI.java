package com.spartaglobal.samurah.view;

import com.spartaglobal.samurah.controller.CSVReader;
import com.spartaglobal.samurah.controller.ConsoleCore;
import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import com.spartaglobal.samurah.model.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleUI {

    private static final Logger logger = LogManager.getLogger(ConsoleUI.class);
    static {
        logger.trace(ConsoleUI.class + " initialized.");
    }

    /***
     * VIEW
     */
    public static void showCommands() {
        logger.debug("SHOWING COMMANDS");
        ConsoleCore.getCommands().forEach((consoleCommand, command) -> printHelper(command));
    }

    public static void printHelper(Command command) {
        logger.debug("SHOWING HELPER FOR: "+ command);
        System.out.printf("%s - %s%n", command.getCommand(), command.getHelper());
    }//print the helper for the command

    public static void printHelper() {
        logger.debug("SHOWING HELPER");
        System.out.println("Type 'commands' for a list with available commands.");
    }

    public static void printCommandUnknown() {
        logger.debug("Ups.. unknown command.");
        System.err.println("Unknown command.");
        printHelper();
    }

    public static void printMessage(String message) {
        logger.debug("Printing message: "+message);
        System.out.println(message);
    }

    public static void printError(String message) {
        logger.error("Printing message: "+message);
        System.err.println(message);
    }
}
