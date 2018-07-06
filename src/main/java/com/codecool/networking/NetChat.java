package com.codecool.networking;

import com.codecool.networking.modes.AppMode;
import com.codecool.networking.modes.Client;
import com.codecool.networking.modes.Server;
import com.codecool.networking.modes.terminalClient.BasicClient;
import com.codecool.networking.modes.server.BasicServer;
import com.codecool.networking.terminalView.BasicView;
import com.codecool.networking.terminalView.TerminalView;

import java.io.IOException;

public class NetChat {

    public static void main(String[] args) {

        TerminalView view = new BasicView();

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

    private static Client createClient(TerminalView view, String[] args, int port)
            throws IndexOutOfBoundsException, IOException {

        if (args.length != 4) {
            displayInvalidArgumentInfo(view);
            throw new IllegalArgumentException();
        }

        final int IP_INDEX = 2;
        final int USER_NAME_INDEX = 3;

        String ipAddress = args[IP_INDEX];
        String userName = args[USER_NAME_INDEX];

        return BasicClient.create(ipAddress, port, userName, view);
    }

    private static Server createServer(NetView view, int port) {
        view.display("Creating server...");
        return BasicServer.createMultiUserServer(port);
    }

    private static void displayInvalidArgumentInfo(NetView view) {
        view.display("Invalid arguments");
        view.displayHelp();
    }
}
