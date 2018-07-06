package com.codecool.networking.modes.terminalClient;

import com.codecool.networking.modes.Client;
import com.codecool.networking.modes.MagicWords;
import com.codecool.networking.terminalView.TerminalView;

import java.io.*;
import java.net.Socket;

public class BasicClient implements TerminalClient {

    private final String userName;
    private final TerminalView view;
    private Socket socket;

    public static Client create(String ipAddress, int port, String userName, TerminalView view) throws IOException {
        return new BasicClient(ipAddress, port, userName, view);
    }

    private BasicClient(String ipAddress, int port, String userName, TerminalView view) throws IOException {
        this.socket = new Socket(ipAddress, port);
        this.userName = userName;
        this.view = view;
    }

    @Override
    public void start() {

        MagicWords spellToExitChat = MagicWords.QUIT_CHAT_WORD;
        new Thread(new ClientReader(socket, view, spellToExitChat), userName).start();
        new Thread(new ClientWriter(socket, view, userName, spellToExitChat), userName).start();

    }
}
