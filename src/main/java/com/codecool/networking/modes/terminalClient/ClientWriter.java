package com.codecool.networking.modes.terminalClient;

import com.codecool.networking.data.Message;
import com.codecool.networking.terminalView.TerminalView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Helper class for terminal client (to send messages)
 */
class ClientWriter implements Runnable {

    private final TerminalClient client;
    private final Socket socket;
    private final TerminalView view;
    private final String userName;

    ClientWriter(TerminalClient client, Socket socket, TerminalView view, String userName) {
        this.client = client;
        this.socket = socket;
        this.view = view;
        this.userName = userName;
    }

    @Override
    public void run() {

        String QUIT_CHAT_WORD = ".END";
        String info = String.format("Type Your message (or %s to exit chat): ", QUIT_CHAT_WORD);

        try ( ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream()) ) {

            String typedMessage = "";

            while (! typedMessage.equals(QUIT_CHAT_WORD) ) {

                view.display(info);
                typedMessage = view.getInput();

                Message newMessage = new Message(typedMessage, userName);
                if (! socket.isClosed() ) {
                    outputStream.writeObject(newMessage);
                    outputStream.flush();
                }

                if (typedMessage.equals(QUIT_CHAT_WORD)) {
                    break;
                }
            }

        } catch (IOException e) {
            view.display("[Connection problem occurred]:");
            e.printStackTrace();

        }

        client.shutDown();
    }
}
