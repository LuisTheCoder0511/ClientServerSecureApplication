package com.application;

import com.application.service.ClientService;
import com.application.input.NetworkInput;
import com.application.window.Window;

public class ClientMain {
    public static void main(String[] args) {
        NetworkInput input = new NetworkInput();
        try (ClientService clientService = new ClientService(input)){
            Window window = new Window(clientService, "Client: " + input.getName(), 800, 600);
            clientService.assignWindow(window, input.getName());
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}