package com.codecool.networking.modes.client;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class BasicClient implements Client {

    private final Socket socket;
    private final String userName;


    public static Client create(String ipAddress, int port, String userName) throws IOException {
        return new BasicClient(ipAddress, port, userName);
    }

    private BasicClient(String ipAddress, int port, String userName) throws IOException {
        this.socket = new Socket(ipAddress, port);
        this.userName = userName;
    }


    @Override
    public void start() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Client, start");

        try (
                ObjectInputStream inputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
                ObjectOutputStream outputStream = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
        ) {

            String userInput = "";

            System.out.println("Client, created streams");

            while (! userInput.equals(".END") ) {

                System.out.println("Client, Entered loop");

                userInput = scanner.nextLine();

                Message createdMessage = new Message(userInput, userName);

                System.out.println("Client, created message: " + createdMessage);

                outputStream.writeObject(createdMessage);
                outputStream.flush();

                Message message = (Message) inputStream.readObject();

                System.out.println("Client, input message: " + message);


            }

            System.out.println("Client: exiting");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
