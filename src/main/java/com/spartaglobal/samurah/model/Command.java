package com.spartaglobal.samurah.model;

public enum Command {
    COMMANDS("commands", "displays this list"),
    IMPORT("import", "\"import [file].csv\" will try to import the [file].csv from resources/", "\\w.csv"),
    VALIDATE("validate", "validates imported data, preparing it for 'push' to database"),
    STATUS("status", "prompts the current number of valid/invalid/corrupt entries"),
    PUSH("push", "pushes the valid entries to the database"),
    EXPORT_INVALID("export-invalid", "usage: export-invalid [filename].csv exports invalid entries to a csv file in resources/ directory", "invalid"),
    EXPORT_CORRUPT("export-corrupt", "usage: export-corrupt [filename].csv exports corrupt entries to a csv file in resources/ directory", "corrupt"),
    QUIT("quit", "stops the execution of the program");

    private final String[] args;
    private final String command;
    private final String helper;

    Command(String command, String helper, String... args) {
        this.command = command;
        this.helper = helper;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommand() {
        return command;
    }

    public String getHelper() {
        return helper;
    }
}
