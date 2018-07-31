package com.codecool.networking.modes.terminalClient;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.MagicWords;
import com.codecool.networking.terminalView.TerminalView;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;


/**
 * Helper class for terminal client (to display messages)
 */
class ClientReader implements Runnable {

    private Socket socket;
    private final TerminalView view;
    private boolean shouldExit;
    private final String quitChatWord;

    ClientReader(Socket socket, TerminalView view, MagicWords quitChatWord) {
        this.socket = socket;
        this.view = view;
        this.quitChatWord = quitChatWord.getWord();
    }

    @Override
    public void run() {

        try ( ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ) {
            while (! shouldExit) {
                try {

                    Message message = (Message) inputStream.readObject();
                    shouldExit = checkIfShouldQuit(message);
                    view.display(message);

                } catch (SocketException | EOFException notUsed) {
                    shouldExit = true;
                    view.display("Exiting chat...");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            view.display("[Connection problem occurred]");
        }
    }

    private boolean checkIfShouldQuit(Message message) {
        if (message != null) {
            return message.getContent().equals(quitChatWord);
        }
        return true;
    }
}
