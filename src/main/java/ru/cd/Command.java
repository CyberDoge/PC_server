package ru.cd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class Command implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private final static Runtime RUNTIME = Runtime.getRuntime();
    private Connection connection;
    private String command;
    private long startingDate;
    private Process process;
    private InputStream reader;
    private OutputStream writer;

    public Command(Connection connection, String command) {
        this.connection = connection;
        this.command = command;
        startingDate = new Date().getTime();
    }


    @Override
    public void run() {
        try {
            System.out.println(ANSI_BLUE + command + ANSI_RESET);
            process = RUNTIME.exec(command);
            reader = process.getInputStream();
            writer = process.getOutputStream();
            var result = new String(reader.readAllBytes());
            System.out.println(ANSI_GREEN + result + ANSI_RESET);
            connection.writeResult(result);
        } catch (IOException e) {
            try {
                connection.writeResult(command + " exec error:\n" + e.getLocalizedMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public void sendInfo() {
        StringBuilder builder = new StringBuilder();
    }
}
