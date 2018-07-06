package com.codecool.networking.terminalView;

import com.codecool.networking.data.Message;

import java.util.Scanner;

public class BasicView implements TerminalView {


    @Override
    public void display(String text) {
        System.out.println(text);
    }

    @Override
    public void display(Message message) {
        System.out.println(message);
    }

    @Override
    public String getInput(String message) {
        System.out.println();
        System.out.print(message);
        return getInput();
    }

    @Override
    public String getInput() {

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while(userInput.length() == 0) {
            userInput = scanner.nextLine();
        }
        return userInput;
    }

    @Override
    public void displayHelp() {

        System.out.println();
        System.out.println("**********************************************************");
        System.out.println("Command line arguments: ");
        System.out.println("    - as server: server [port], eg.: server 9999 (will run app as server using port 9999");
        System.out.println("    - as client: client [port] [ipAddress] [user name]," +
                "eg.: client 198.168.1.20 9999 John (will run app as client" +
                " using port 9999 with server's ip 198.168.1.20 as John");
    }
}
