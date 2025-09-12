package com.application.service;

import com.application.window.Window;

public abstract class ServiceClass {
    public void windowService(Window window, String name) throws InterruptedException {
        while (!window.isClosed()){
            if (!window.getText().isBlank()){
                String text = name + ": " + window.getText();
                window.pushMessage(text);
                System.out.println(text);
                sendMessage(text);
                window.resetText();
            }
            Thread.sleep(100);
        }
    }

    public abstract void sendMessage(String message);

    public abstract void close();
}