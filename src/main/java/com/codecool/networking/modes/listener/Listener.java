package com.codecool.networking.modes.listener;

import com.codecool.networking.data.Message;

import java.util.Collection;

public interface Listener extends Runnable {

    Collection<Message> gather();
    void shutDown();
    boolean hasMessage();
}
