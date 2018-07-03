package com.codecool.networking;

import com.codecool.networking.modes.AppMode;
import com.codecool.networking.modes.Client;
import com.codecool.networking.modes.Server;
import com.codecool.networking.modes.client.SimpleClient;
import com.codecool.networking.modes.listener.Listener;
import com.codecool.networking.modes.listener.SimpleListener;
import com.codecool.networking.modes.sender.Sender;
import com.codecool.networking.modes.sender.SimpleSender;
import com.codecool.networking.modes.server.SimpleServer;
import com.codecool.networking.modes.view.NetView;
import com.codecool.networking.modes.view.SimpleView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NetChat {

    public static void main(String[] args) {

        NetView view = new SimpleView();

        if (args.length < 2) {
            view.displayHelp();
            return;
        }

        view.display("Welcome to NetChat");

        final int MODE_INDEX = 0;
        final int PORT_INDEX = 1;

        String mode;
        int port;

        try {
            mode = args[MODE_INDEX];
            port = Integer.parseInt(args[PORT_INDEX]);

            AppMode appMode = AppMode.valueOf(mode.toUpperCase());

            switch (appMode) {
                case CLIENT:
                    createClient(view, args, port).start();
                    break;

                case SERVER:
                    createServer(view, port).start();
                    break;

                default:
                    displayInvalidArgumentInfo(view);
                    break;
            }

        } catch (IndexOutOfBoundsException | IllegalArgumentException ex) {
            displayInvalidArgumentInfo(view);
        } catch (IOException e) {
            view.display("Something goes wrong with connection, error message:");
            e.printStackTrace();
        }
    }

    private static Client createClient(NetView view, String[] args, int port)
            throws IndexOutOfBoundsException, IOException {

        if (args.length != 4) {
            displayInvalidArgumentInfo(view);
            throw new IllegalArgumentException();
        }

        final int IP_INDEX = 2;
        final int USER_NAME_INDEX = 3;

        String ipAddress = args[IP_INDEX];
        String userName = args[USER_NAME_INDEX];

        view.display("Connecting to server...");
        try (Socket socket = new Socket(ipAddress, port) ) {
            view.display(String.format("Connection created using: %s [address], %s [port]", ipAddress, port));

            Listener listener = new SimpleListener(socket);
            Sender sender = new SimpleSender(socket);

            return new SimpleClient(userName, view, listener, sender);
        }
    }

    private static Server createServer(NetView view, int port) throws IOException {

        // gather ipAddress
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();

        view.display("Creating server...");
        try (Socket socket = new Socket(ipAddress, port) ) {
            view.display(String.format("Server created using port %s", port));

            Listener listener = new SimpleListener(socket);
            Sender sender = new SimpleSender(socket);

            return new SimpleServer(listener, sender);
        }
    }

    private static void displayInvalidArgumentInfo(NetView view) {
        view.display("Invalid arguments");
        view.displayHelp();
    }
}
