package com.codecool.networking.modes.client;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleClient implements Client {

    private final String ipAddress;
    private final int port;
    private final String userName;

    public SimpleClient(String ipAddress, int port, String userName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.userName = userName;
    }

    @Override
    public void start() throws IOException {

        Socket socket = new Socket(ipAddress, port);

        Message message = new Message("lalallllaaa", userName);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
        objectOutputStream.close();

    }
}
