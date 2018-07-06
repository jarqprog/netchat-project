package com.codecool.networking.modes.server;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.MagicWords;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

class UserSocket implements Runnable {

    private final Socket userSocket;
    private final MultiUserServer server;
    private String userName;
    private boolean shouldQuit;
    private final String quitChatWord;

    UserSocket(Socket userSocket, MultiUserServer server, MagicWords quitChatWord) {
        this.userSocket = userSocket;
        this.server = server;
        this.quitChatWord = quitChatWord.getWord();
        this.userName = "";
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream outputStream = new ObjectOutputStream(new DataOutputStream(userSocket
                        .getOutputStream()));
                ObjectInputStream inputStream = new ObjectInputStream(new DataInputStream(userSocket
                        .getInputStream()))) {

            tryToRegister(inputStream, outputStream);

            while (! shouldQuit) {
                Message message = (Message) inputStream.readObject();
                if (! broadcastMessage(message, outputStream)) {
                    System.out.println("Message: " + message + " wasn't sent");
                }

                shouldQuit = checkIfShouldQuit(message);
            }
        } catch (IOException | ClassNotFoundException notUsed) {
            System.out.println("Shutting down the process for " + userName);
        } finally {
            unregisterFromServer();
            System.out.println(userName + " unregistered");
            closeSocket();
        }
    }

    String getUserName() {
        return userName;
    }

    private void unregisterFromServer() {
        server.getUsers().remove(this);
    }

    private boolean broadcastMessage(Message message, ObjectOutputStream outputStream) throws IOException {
        if (message == null || userSocket.isClosed()) {
            return false;
        }
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (SocketException notUsed) {
            return false;
        }
        return true;
    }

    private void tryToRegister(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException {

        if (registerOnServerAndSayHello(inputStream, outputStream)) {
            System.out.println("User registered on server");
        } else {
            System.out.println("Registration failed");
            shouldQuit = true;
            throw new IOException();
        }

    }

    private boolean registerOnServerAndSayHello(ObjectInputStream inputStream, ObjectOutputStream outputStream) {

        try {
            Message message = (Message) inputStream.readObject();
            if (message != null) {
                this.userName = message.getAuthor();
                this.server.registerUser(this);
                broadcastMessage(message, outputStream);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void closeSocket() {

        try {
            if (userSocket != null && ! userSocket.isClosed()) {
                userSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfShouldQuit(Message message) {
        if (message != null) {
            return message.getContent().equals(quitChatWord);
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        UserSocket userSocket = (UserSocket) o;
        return Objects.equals(userName, userSocket.getUserName());
    }
}