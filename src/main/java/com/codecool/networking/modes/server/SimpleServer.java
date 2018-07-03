package com.codecool.networking.modes.server;

import com.codecool.networking.modes.Server;
import com.codecool.networking.modes.listener.Listener;
import com.codecool.networking.modes.sender.Sender;

public class SimpleServer implements Server {

    private final Listener listener;
    private final Sender sender;

    public SimpleServer(Listener listener, Sender sender) {
        this.listener = listener;
        this.sender = sender;
    }

    @Override
    public void start() {



    }
}
