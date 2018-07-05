package com.codecool.networking.modes.server;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BasicServer implements Server {

    private final int port;

    public static Server create(int port) throws IOException {
        return new BasicServer(port);
    }

    private BasicServer(int port) throws IOException {
        this.port = port;
    }

    @Override
    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is active, waiting for clients...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Client connected!");

                try (
                        ObjectInputStream inputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream()) ) {

                    while (true) {

                        Message message = (Message) inputStream.readObject();
                        System.out.println("Message received: " + message);

                        if (message != null) {
                            outputStream.writeObject(message);
                            outputStream.flush();
                            if (message.getContent().equals(".END")) {
                                break;
                            }
                        }
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException notUsed) {
                    System.out.println("Client disconnected");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
