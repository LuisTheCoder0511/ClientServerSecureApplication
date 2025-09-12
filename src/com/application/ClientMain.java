package com.application;

import com.application.service.ClientService;
import com.application.input.NetworkInput;
import com.application.window.Window;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
        NetworkInput input = new NetworkInput();
        Window window = new Window("Client: " + input.getName(), 800, 600);
        ClientService clientService = new ClientService(window, input);
        clientService.windowService(window, input.getName());
        clientService.close();
    }
}