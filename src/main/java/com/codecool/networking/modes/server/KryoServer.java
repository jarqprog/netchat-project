package com.codecool.networking.modes.server;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Server;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class KryoServer implements Server {


    private final int port;

    public KryoServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException, ClassNotFoundException {

        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        Kryo kryo = new Kryo();

        try (Input input = new Input(socket.getInputStream())) {
            kryo.register(Message.class);
            kryo.register(LocalDateTime.class);

            Message message = kryo.readObject(input, Message.class);

            System.out.println(message);
        }

    }
}
