package com.application;

import com.application.input.NetworkInput;
import com.application.service.ServerService;
import com.application.window.Window;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServerMain {
    public static void main(String[] args) throws InterruptedException, IOException, NoSuchAlgorithmException {
        String name = "Server";
        NetworkInput input = new NetworkInput(name);
        Window window = new Window(name, 800, 600);
        ServerService server = new ServerService(window, input);
        server.windowService(window, name);
        server.close();
    }
}