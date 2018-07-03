package com.codecool.networking.modes.listener;

import com.codecool.networking.data.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SimpleListener implements Listener {

    private final Socket socket;
    private final Queue<Message> messages = new LinkedList<>();
    private boolean shouldExit;

    public SimpleListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Collection<Message> gather() {
        List<Message> toReturn = new LinkedList<>();
        toReturn.addAll(messages);
        messages.remove();
        return toReturn;
    }

    @Override
    public void shutDown() {
        shouldExit = true;
    }

    @Override
    public boolean hasMessage() {
        return messages.size() > 0;
    }

    @Override
    public void run() {

        int waitTime = 200;

        try (ObjectInputStream os = new ObjectInputStream(socket.getInputStream()) ) {

            while (! shouldExit) {
                Message message = (Message) os.readObject();

                if (message != null) {
                    messages.add(message);
                }
                wait(waitTime);
            }

        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
