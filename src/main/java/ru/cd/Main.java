package ru.cd;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<Integer, Command> COMMANDS = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Connection connection = new Connection();

        while (true) {
            String input = connection.readCommand();
            if (input.startsWith("id:")) {
                var indexOfStart = input.indexOf(',');
                var id = Integer.parseInt(input.substring(3, indexOfStart));
                var command = COMMANDS.get(id);
                var request = input.substring(indexOfStart+1);
                command.putRequest(request);
            }
            Command cmd = new Command(connection, input);
            COMMANDS.put(cmd.getId(), cmd);
            new Thread(cmd).start();
        }
    }
}