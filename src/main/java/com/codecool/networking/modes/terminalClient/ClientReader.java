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
        String connectionProblemInfo = "[Connection problem occurred]";

        try ( ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ) {
            while (! shouldExit) {
                try {
                    Message message = (Message) inputStream.readObject();
                    shouldExit = checkIfShouldQuit(message);
                    if (! shouldExit) {
                        view.display(message);
                    }
                } catch (SocketException | EOFException notUsed) {
                    view.display(connectionProblemInfo);
                    shouldExit = true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            view.display(connectionProblemInfo);
            e.printStackTrace();
        }
    }

    private boolean checkIfShouldQuit(Message message) {
        if (message != null) {
            return message.getContent().equals(quitChatWord);
        }
        return true;
    }
}
