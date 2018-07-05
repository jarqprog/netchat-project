package com.codecool.networking.modes.terminalClient;

import com.codecool.networking.data.Message;
import com.codecool.networking.terminalView.TerminalView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


/**
 * Helper class for terminal client (to display messages)
 */
class ClientReader implements Runnable {

    private final TerminalClient client;
    private Socket socket;
    private final TerminalView view;

    ClientReader(TerminalClient client, Socket socket, TerminalView view) throws IOException {
        this.client = client;
        this.socket = socket;
        this.view = view;
    }

    @Override
    public void run() {

        try ( ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ) {

            while (client.isAlive()) {

                Message message = (Message) inputStream.readObject();
                if (message != null) {
                    view.display(message);
                }
            }
        } catch (IOException e) {
            view.display("[Connection problem occurred]:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            view.display("[Problem occurred with retrieving the message]:");
            e.printStackTrace();
        }
    }
}
