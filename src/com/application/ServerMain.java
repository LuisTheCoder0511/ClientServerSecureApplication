package com.application;

import com.application.input.NetworkInput;
import com.application.service.ServerService;
import com.application.window.Window;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServerMain {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String name = "Server";
        NetworkInput input = new NetworkInput(name);
        try (ServerService server = new ServerService(input)) {
            Window window = new Window(server, name, 800, 600);
            server.assignWindow(window, name);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}