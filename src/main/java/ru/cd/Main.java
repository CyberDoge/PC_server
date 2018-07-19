package ru.cd;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Connection connection = new Connection();
       while (true){
           String command = connection.readCommand();
           Command cmd = new Command(connection, command);
           new Thread(cmd).start();
       }
    }
}