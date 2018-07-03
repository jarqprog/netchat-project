package com.codecool.networking.modes.sender;

import com.codecool.networking.data.Message;

public interface Sender extends Runnable {

    void send(Message message);
    void shutDown();

}
