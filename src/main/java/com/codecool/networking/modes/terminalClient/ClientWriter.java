package com.codecool.networking.modes.terminalClient;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.MagicWords;
import com.codecool.networking.terminalView.TerminalView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Helper class for terminal client (to send messages)
 */
class ClientWriter implements Runnable {

    private final Socket socket;
    private final TerminalView view;
    private final String userName;
    private final String quitChatWord;
    private boolean shouldExit;

    ClientWriter(Socket socket, TerminalView view, String userName, MagicWords quitChatWord) {
        this.socket = socket;
        this.view = view;
        this.userName = userName;
        this.quitChatWord = quitChatWord.getWord();
    }

    @Override
    public synchronized void run() {
        try ( ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream()) ) {

            tryToRegister(outputStream);
            String info = String.format("Type Your message (or '%s' to exit chat): ", quitChatWord);
            view.display(info);

            while (! shouldExit) {

                String typedMessage = view.getInput();
                Message newMessage = new Message(typedMessage, userName);
                outputStream.writeObject(newMessage);
                outputStream.flush();
                shouldExit = checkIfShouldQuit(typedMessage);
            }

        } catch (IOException notUsed) {
            shouldExit = true;
        } finally {
            closeSocket();
        }
    }

    private void tryToRegister(ObjectOutputStream outputStream) throws IOException {
        if (sendRegistrationMessageToServer(outputStream)) {
            String connectionInfo = String.format("Connected to server: [%s]. Registration succeed.", socket.getInetAddress());
            view.display(connectionInfo);
        } else {
            view.display("Registration failed, shutting down the app...");
            throw new IOException();
        }
    }

    private boolean sendRegistrationMessageToServer(ObjectOutputStream outputStream) {
        Message registrationMessage = new Message(userName+ " has joined the chat", userName);
        try {
            outputStream.writeObject(registrationMessage);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkIfShouldQuit(String messageContent) {
        return messageContent.equals(quitChatWord);
    }

    private void closeSocket() {
        if (socket != null && ! socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
