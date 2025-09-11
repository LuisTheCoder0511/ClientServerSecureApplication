package com.application;

import com.application.network.Client;
import com.application.window.Window;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientMain {
    private static ScheduledExecutorService readService = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws IOException, InterruptedException {
        String nameInput = null;
        while (nameInput == null) {
            nameInput = JOptionPane.showInputDialog("Enter your name:");
        }
        Window window = new Window("Client: " + nameInput, 800, 600);
        Client client = new Client();
        client.open();
        readService.scheduleAtFixedRate(() -> {
            try {
                String line = client.read();
                if (line != null) {
                    window.pushMessage(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 250, TimeUnit.MILLISECONDS);

        while (!window.isClosed()) {
            if (!window.getText().isBlank()) {
                String text = nameInput + ": " + window.getText();
                window.resetText();
                client.write(text);
                System.out.println("Out: " + text);
            } else {
                client.write("");
            }
            Thread.sleep(250);
        }
        client.close();
    }
}
