package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.Starter;
import com.spartaglobal.samurah.model.EmployeeDTO;
import com.spartaglobal.samurah.model.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

public class CSVExporter {

    static{
        Logger logger = LogManager.getLogger(CSVExporter.class);
        logger.trace(CSVExporter.class + " initialized.");
    }

    private static boolean createFile(String filename) throws IOException {
        String path = "src/main/resources/";
        File file = new File(path + filename);
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
            return true;
        } else {
            System.out.println("File already exists. ");
            return false;
        }
    }

    private static boolean isOverwriting() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Overwrite?");
        do {
            System.out.print("(y/n): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                return false;
            }
        } while (true);
    }

    private static void fileExporter(String filename, String text) throws IOException {
        boolean writingAccess = true;
        String path = "src/main/resources/";
        if (!createFile(filename)) {
            writingAccess = isOverwriting();
        }
        if (writingAccess) {
            try {
                FileWriter fileWriter = new FileWriter(path+ filename);
                fileWriter.write(text);
                fileWriter.close();
            } catch (IOException e) {
                System.err.println("Export failed.");
                return;
            }
            System.out.println("Successfully exported.");
        }
    }

    public static void exportInvalid(String filename, Collection<EmployeeDTO> employees) throws IOException {
        StringBuilder text = new StringBuilder();
        text.append(EmployeeRepository.csvHeader).append("\n");
        for (EmployeeDTO employee : employees) {
            text.append(CSVConverter.convertToCsvLine(employee)).append("\n");
        }
        fileExporter(filename, text.toString());
    }

    public static void exportCorrupt(String filename, Collection<String> corruptData) throws IOException {
        StringBuilder text = new StringBuilder();
        text.append(EmployeeRepository.csvHeader).append("\n");
        for (String data : corruptData) {
            text.append(data).append("\n");
        }
        fileExporter(filename, text.toString());
    }

}
