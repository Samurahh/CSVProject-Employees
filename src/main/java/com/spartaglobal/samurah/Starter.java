package com.spartaglobal.samurah;

import com.spartaglobal.samurah.controller.ConsoleCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Starter {
    /*
    THREADS NOT STABLE! PLEASE DO NOT EXCEED 10 THREADS. (SINGLE THREAD - MOST EFFICIENT)
     */
    public static final int THREADS = 4;

    static{
        Logger logger = LogManager.getLogger(Starter.class);
        logger.trace(Starter.class + " initialized.");
    }


    public static void start(){
        ConsoleCore.start();
        long initial = System.nanoTime();
        ConsoleCore.tryImport("EmployeeRecordsLarge.csv");
        ConsoleCore.validate();
        ConsoleCore.pushToDB();
        long finalTime = System.nanoTime();
        System.err.println("Time taken: "+ (finalTime-initial)/1_000_000+" ms.");
    }
}
