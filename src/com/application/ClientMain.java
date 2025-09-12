package com.application;

import com.application.network.ClientService;
import com.application.window.Window;

import javax.swing.*;
import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        String nameInput = null;
        while (nameInput == null) {
            nameInput = JOptionPane.showInputDialog("Enter your name:");
        }
        Window window = new Window("Client: " + nameInput, 800, 600);
        ClientService clientService = new ClientService(window);
        clientService.windowService(window, nameInput);
        clientService.close();
    }
}