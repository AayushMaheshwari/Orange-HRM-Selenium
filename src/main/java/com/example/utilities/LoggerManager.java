package com.example.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {
    //Method to return Logger for provided class
    public static Logger getLogger(Class<?> className){
        return LogManager.getLogger();
    }
}
