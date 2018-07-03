package com.codecool.networking.modes.client;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Client;
import com.codecool.networking.modes.listener.Listener;
import com.codecool.networking.modes.sender.Sender;
import com.codecool.networking.modes.view.NetView;

public class SimpleClient implements Client {

    private final String userName;
    private final NetView view;
    private final Listener listener;
    private final Sender sender;

    public SimpleClient(String userName, NetView view, Listener listener, Sender sender) {
        this.userName = userName;
        this.view = view;
        this.listener = listener;
        this.sender = sender;
    }

    @Override
    public void start() {

        String userInput = "";
        String quitInput = "q";
        Message message;


        while ( ! userInput.toLowerCase().equals(quitInput) ) {

            userInput = view.getInput();  // message content
            message = new Message(userInput, userName);
            sender.send(message);

            notifyAll();

        }

    }
}
