package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MyLogging implements Logger {

    String filename;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("M/d/yyyy HH:mm:ss");

    public Logging(String filename){
        this.filename = filename;
    }

    @Override
    public void info(String message){
        try(FileWriter log = new FileWriter(filename, true)){
            String date = DATE_FORMAT.format(System.currentTimeMillis());
            log.write(date + "  " + message + "\r\n");
        } catch(IOException e){
            System.err.println("error while writing log : " + e.getMessage());
        }
    }

    @Override
    public void error(String message, Throwable e){
        try(FileWriter log = new FileWriter(filename, true)){
            String date = DATE_FORMAT.format(System.currentTimeMillis());
            log.write(date + " " + message + "\r\n");
            log.write(date + " " + e.getMessage() + "\r\n");
        } catch(IOException e){
            System.err.println("error while writing log: " + e.getMessage());
        }
    }
}