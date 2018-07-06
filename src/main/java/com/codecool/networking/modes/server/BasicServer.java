package com.codecool.networking.modes.server;

import com.codecool.networking.modes.MagicWords;
import com.codecool.networking.modes.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class BasicServer implements MultiUserServer {

    private final int port;
    private volatile Set<UserServer> users;

    public static MultiUserServer createMultiUserServer(int port) {
        return new BasicServer(port);
    }

    public static Server createServer(int port) {
        return new BasicServer(port);
    }

    private BasicServer(int port) {
        this.port = port;
        this.users = new HashSet<>();
    }

    @Override
    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is active, waiting for clients...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("SERVER: Client connected!");
                MagicWords spellToExitChat = MagicWords.QUIT_CHAT_WORD;
                new Thread(new UserServer(socket, this, spellToExitChat)).start();
                System.out.println(users);
            }
        } catch (IOException e) {
            System.out.println("Couldn't run server!");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Set<UserServer> getUsers() {
        return users;
    }

    @Override
    public boolean registerUser(UserServer userSocket) {
        if (userSocket.getUserName().length() == 0 || this.users.contains(userSocket)) {
            return false;
        }
        return this.users.add(userSocket);
    }
}
