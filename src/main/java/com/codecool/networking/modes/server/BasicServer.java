package com.codecool.networking.modes.server;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BasicServer implements Server {

    private final ServerSocket serverSocket;

    public static Server create(int port) throws IOException {
        return new BasicServer(port);
    }

    private BasicServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void start() {

        Message message;

        System.out.println("Waiting for clients");
        try (Socket socket = serverSocket.accept()) {

            System.out.println("Server - client connected");

            try (
                    ObjectOutputStream outputStream = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
                    ObjectInputStream inputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream())) ) {

                while ( (message = (Message) inputStream.readObject() ) != null) {


                    System.out.println("Server, message: " + message);

                    outputStream.writeObject(message);
                    outputStream.flush();

                    if (message.getContent().equals(".END")) {
                        System.out.println("server, exiting");
                        break;
                    }

                }

                System.out.println("Exiting server");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
