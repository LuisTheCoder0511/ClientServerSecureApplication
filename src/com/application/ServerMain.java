package com.application;

import com.application.network.ServerService;
import com.application.window.Window;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServerMain {
    public static void main(String[] args) throws InterruptedException, IOException, NoSuchAlgorithmException {
        Window window = new Window("ServerService", 800, 600);
        ServerService server = new ServerService(window);
        server.windowService(window, "ServerService");
        server.close();
    }
}