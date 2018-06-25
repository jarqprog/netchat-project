package com.codecool.networking.modes.server;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer implements Server {

    private final int port;

    public SimpleServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException, ClassNotFoundException {

        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();


        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {

            Message message = (Message) objectInputStream.readObject();

            System.out.println(message);
        }

    }
}
