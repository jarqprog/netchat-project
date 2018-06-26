package com.codecool.networking.modes.client;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Client;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class KryoClient implements Client {

    private final String ipAddress;
    private final int port;
    private final String userName;

    public KryoClient(String ipAddress, int port, String userName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.userName = userName;
    }

    @Override
    public void start() throws IOException {

        Socket socket = new Socket(ipAddress, port);

        Kryo kryo = new Kryo();

        Message message = new Message("lalallllaaa", userName);

        Output output = new Output(socket.getOutputStream());
        kryo.register(LocalDateTime.class);
        kryo.register(Message.class);
        kryo.writeObject(output, message);
        output.close();

    }
}
