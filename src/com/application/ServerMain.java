package com.application;

import com.application.network.Server;
import com.application.window.Window;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Window window = new Window("Server", 800, 600);
        Server server = new Server(window);
        server.acceptRequests(window);
    }
}
