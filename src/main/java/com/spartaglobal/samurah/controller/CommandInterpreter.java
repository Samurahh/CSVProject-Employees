package com.spartaglobal.samurah.controller;

import com.spartaglobal.samurah.model.Command;
import com.spartaglobal.samurah.view.ConsoleUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandInterpreter {
    private static final Logger logger = LogManager.getLogger(CommandInterpreter.class);

    static {
        logger.trace(CommandInterpreter.class + " initialized.");
    }

    /***
     * Command CONTROLLER
     */

    public static void execute(Command command, String... args) {
        if (checkArgs(command, args)) {
            if (command == null) {
                logger.debug("CommandInterpreter - UNKNOWN COMMAND");
                ConsoleUI.printCommandUnknown();
            } else {
                logger.debug("CommandInterpreter - "+ command);
                switch (command) {
                    case COMMANDS: {
                        ConsoleUI.showCommands();
                        break;
                    }
                    case IMPORT: {
                        ConsoleCore.tryImport(args[0]);
                        break;
                    }
                    case VALIDATE: {
                        ConsoleCore.validate();
                        break;
                    }
                    case STATUS: {
                        ConsoleCore.checkStatus();
                        break;
                    }
                    case PUSH: {
                        ConsoleCore.pushToDB();
                        break;
                    }
                    case EXPORT_INVALID: {
                        ConsoleCore.exportToFileInvalid(args[0]);
                        break;
                    }
                    case EXPORT_CORRUPT: {
                        ConsoleCore.exportToFileCorrupt(args[0]);
                        break;
                    }
                    case QUIT: {
                        ConsoleCore.stopExecution();
                        break;
                    }
                }
            }
        } else {
            logger.debug("CommandInterpreter - "+ command);
            ConsoleUI.printHelper(command);
        }
    }

    private static boolean checkExportArgs(String[] args) {
        return args.length == 3;
    }

    private static boolean checkArgs(Command command, String... args) {
        if (command == null) {
            return true;
        }
        if (command.getArgs().length > 0) {
            if (args == null) {
                return false;
            } else {
                return args.length <= 1;
            }
        } else {
            return args == null;
        }
    }
}
