package com.codecool.networking.modes.server;

import com.codecool.networking.modes.Server;

import java.util.Set;

interface MultiUserServer extends Server {

    Set<UserSocket> getUsers();
    boolean registerUser(UserSocket userSocket);
}
