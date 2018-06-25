package com.codecool.networking;

import com.codecool.networking.modes.AppMode;
import com.codecool.networking.modes.client.SimpleClient;
import com.codecool.networking.modes.server.SimpleServer;

import java.io.IOException;

public class NetChat {


    public static void main(String[] args) throws IOException, ClassNotFoundException {


        final int MODE_INDEX = 0;
        final int IP_INDEX = 1;
        final int PORT_INDEX = 2;
        final int USER_NAME_INDEX = 3;


        String mode;
        String ipAddress;
        int port;
        String userName;

        if (args.length < 3) {
            throw new IllegalArgumentException("invalid arguments!");
        }
        try {
            mode = args[MODE_INDEX];
            ipAddress = args[IP_INDEX];
            port = Integer.parseInt(args[PORT_INDEX]);

            AppMode appMode = AppMode.valueOf(mode.toUpperCase());

            switch (appMode) {

                case CLIENT:
                    if (args.length != 4) {
                        throw new IllegalArgumentException("Invalid arguments!");
                    }

                    userName = args[USER_NAME_INDEX];

                    new SimpleClient(ipAddress, port, userName).start();

                    break;
                case SERVER:

                    new SimpleServer(port).start();

                    break;

                default:
                    throw new IllegalArgumentException("Invalid arguments");

            }

        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }


    }
}
