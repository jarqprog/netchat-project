package com.codecool.networking;

import com.codecool.networking.data.Message;

public interface NetView {

    void display(String text);
    void display(Message message);
    String getInput(String message);
    String getInput();
    void displayHelp();

}
