package com.codecool.networking.modes.sender;

import com.codecool.networking.data.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleSender implements Sender {

    private final Socket socket;
    private Message message;
    private boolean shouldExit;

    public SimpleSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send(Message message) {
        this.message = message;
    }

    @Override
    public void shutDown() {
        shouldExit = true;
    }

    @Override
    public void run() {

        try (ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream()) ) {

            while (! shouldExit) {
                if (message != null) {
                    os.writeObject(message);
                    os.flush();
                    message = null;
                }
                wait();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
