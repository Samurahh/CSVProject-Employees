package com.spartaglobal.samurah;


import com.spartaglobal.samurah.controller.CSVReader;
import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LogManager.getLogger(App.class);
    static {
        logger.trace(App.class + " initiated execution.");
    }

    public static void main( String[] args ) {
        Starter.start();
    }

}
