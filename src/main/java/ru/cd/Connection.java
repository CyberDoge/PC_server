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

    public void writeResult(String result) throws IOException {
        writer.writeUTF(result);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        writer.write(bytes);
    }

    public String readCommand() throws IOException {
        return reader.readUTF();
    }
}