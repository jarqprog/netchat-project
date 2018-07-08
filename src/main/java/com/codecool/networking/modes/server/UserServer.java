package com.codecool.networking.modes.server;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.MagicWords;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

class UserServer implements Runnable {

    private final Socket userSocket;
    private final MultiUserServer server;
    private String userName;
    private boolean shouldQuit;
    private final String quitChatWord;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    UserServer(Socket userSocket, MultiUserServer server, MagicWords quitChatWord) {
        this.userSocket = userSocket;
        this.server = server;
        this.quitChatWord = quitChatWord.getWord();
        this.userName = "";
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new DataOutputStream(userSocket
                        .getOutputStream()));
                ObjectInputStream objectInputStream = new ObjectInputStream(new DataInputStream(userSocket
                        .getInputStream()))) {

            setStreams(objectInputStream, objectOutputStream);
            tryToRegister();
            while (! shouldQuit) {
                Message message = (Message) inputStream.readObject();
                if (! broadcastMessage(message)) {
                    System.out.println("The message: " + message + " may not has been sent to everyone");
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

    private boolean sendMessage(Message message) {
        if (message == null) {
            return false;
        }
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException notUsed) {
            return false;
        }
        return true;
    }

    private void setStreams(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.inputStream = objectInputStream;
        this.outputStream = objectOutputStream;
    }

    private boolean broadcastMessage(Message message) {
        if (message == null) {
            return false;
        }
        String content = message.getContent();
        if (! content.equals(quitChatWord)) {
            return sendMessageToAll(message);
        } else {
            return sayThatUserHasLeft();
        }
    }

    private boolean sendMessageToAll(Message message) {
        boolean succeed = true;
        for (UserServer userServer : server.getUsers()) {
            if (! userServer.sendMessage(message) ) {
                succeed = false;
            }
        }
        return succeed;
    }

    private boolean sayThatUserHasLeft() {
        boolean succeed = true;
        Message exitChatMessage = new Message(userName + " has left the chat..", userName);
        for (UserServer userServer : server.getUsers()) {
            if (! userServer.sendMessage(exitChatMessage) ) {
                succeed = userServer == this;  // user socket is closed at this time
            }
        }
        return succeed;
    }

    private void unregisterFromServer() {
        server.getUsers().remove(this);
    }

    private void tryToRegister() throws IOException {

        if (registerOnServerAndSayHello()) {
            System.out.println("User registered on server");
        } else {
            System.out.println("Registration failed");
            shouldQuit = true;
            throw new IOException();
        }

    }

    private boolean registerOnServerAndSayHello() {
        try {
            Message message = (Message) inputStream.readObject();
            if (message != null) {
                userName = message.getAuthor();
                server.registerUser(this);
                showLoggedUsersSet();
                broadcastMessage(message);
                sendInfoAboutLoggedUsers();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showLoggedUsersSet() {
        System.out.println(server.getUsers());
    }

    private void sendInfoAboutLoggedUsers() {
        String loggedUsers = buildInfoAboutLoggedUsers();
        Message chatUsersInfo = new Message(loggedUsers, "[server]");
        sendMessage(chatUsersInfo);
    }

    private String buildInfoAboutLoggedUsers() {
        StringBuilder loggedUsers = new StringBuilder("Chat Users : ");
        for (UserServer user : server.getUsers()) {
            loggedUsers.append(user.getUserName());
            loggedUsers.append(", ");
        }
        int lastCommaIndex = loggedUsers.length()-2;
        return loggedUsers.substring(0, lastCommaIndex);
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
        UserServer userSocket = (UserServer) o;
        return Objects.equals(userName, userSocket.getUserName());
    }

    @Override
    public String toString() {
        return "ServerSocket for Client: " + userName;
    }
}