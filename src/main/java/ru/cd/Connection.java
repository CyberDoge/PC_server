package ru.cd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Connection {
    private static final Integer port = 10000;
    private DataInputStream reader;
    private DataOutputStream writer;
    private ServerSocket serverSocket;

    public Connection() throws IOException {
        serverSocket = new ServerSocket(port);
        var socket = serverSocket.accept();
        System.out.println("connected");
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());
    }

    public void writeResult(String result) {
        try {
            writer.writeUTF(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBytes(byte[] bytes) {
        try {
            writer.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readCommand() {
        try {
            return reader.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}