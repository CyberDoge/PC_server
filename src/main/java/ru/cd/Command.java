package ru.cd;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class Command implements Runnable, Closeable {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static int lastId = 0;

    private Connection connection;

    private String command;
    private long startingDate;
    private Process process;
    private InputStream processReader;
    private OutputStream processWriter;
    private int id;

    public Command(Connection connection, String command) {
        this.connection = connection;
        this.command = command;
        startingDate = new Date().getTime();
        id = lastId++;
    }

    public int getId() {
        return id;
    }

    public boolean isAlive() {
        return process != null && process.isAlive();
    }

    @Override
    public void run() {
        try {
            System.out.println(ANSI_BLUE + command + ANSI_RESET);
            process = RUNTIME.exec(command);
            processReader = process.getInputStream();
            processWriter = process.getOutputStream();
            var output = new String(processReader.readAllBytes());
            System.out.println(ANSI_RED + output.length());
            System.out.println(ANSI_GREEN + output + ANSI_RESET);
            lifeCycle();
        } catch (IOException e) {
            try {
                connection.writeResult(command + " exec error:\n" + e.getLocalizedMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void sendResponse(int id, long pid, long startingDate, String output) throws IOException {
        var wrapper = new CommandJsonWrapper(id, pid, startingDate, output);
        var objMapper = new ObjectMapper();
        var res = objMapper.writeValueAsString(wrapper);
        connection.writeResult(res);
    }

    private void lifeCycle() throws IOException {
        while (process.isAlive()) {
            var processOutput = new String(processReader.readAllBytes());
            sendResponse(id, process.pid(), startingDate, processOutput);
        }
    }


    public void putRequest(String input) throws IOException {
        if (input.equals("kill")) {
            close();
            return;
        }
        processWriter.write(input.getBytes());

    }

    @Override
    public void close() throws IOException {
        //todo check if already called
        processReader.close();
        processWriter.close();
        process.destroy();
    }

    private static final class CommandJsonWrapper {
        String output;
        private int id;
        private long pid;
        private long startingDate;

        public CommandJsonWrapper(int id, long pid, long startingDate, String output) {
            this.id = id;
            this.pid = pid;
            this.startingDate = startingDate;
            this.output = output;
        }
    }
}
