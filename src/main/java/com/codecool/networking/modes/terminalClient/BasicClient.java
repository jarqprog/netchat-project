package com.codecool.networking.modes.terminalClient;

import com.codecool.networking.modes.Client;
import com.codecool.networking.terminalView.TerminalView;

import java.io.*;
import java.net.Socket;

public class BasicClient implements TerminalClient {

    private final String userName;
    private final TerminalView view;
    private Socket socket;
    private boolean isAlive;


    public static Client create(String ipAddress, int port, String userName, TerminalView view) throws IOException {
        return new BasicClient(ipAddress, port, userName, view);
    }

    private BasicClient(String ipAddress, int port, String userName, TerminalView view) throws IOException {
        this.socket = new Socket(ipAddress, port);
        this.userName = userName;
        this.view = view;
        this.isAlive = true;
    }

    @Override
    public void start() throws IOException {


        String connectionInfo = String.format("Connected to server: [%s] ", socket.getInetAddress());

        view.display(connectionInfo);

        new Thread(new ClientReader(this, socket, view), userName).start();
        new Thread(new ClientWriter(this, socket, view, userName), userName).start();
    }

    @Override
    public boolean isAlive() {
        return this.isAlive;
    }

    @Override
    public void shutDown() {
        this.isAlive = false;
        closeSocket();
    }

    private void closeSocket() {
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
