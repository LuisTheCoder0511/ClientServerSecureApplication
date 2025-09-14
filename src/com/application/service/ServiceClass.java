package com.application.service;

import com.application.packet.PacketSMS;
import com.application.window.Window;

public abstract class ServiceClass implements AutoCloseable {
    protected Window window;
    protected String name;

    public void assignWindow(Window window, String name) throws InterruptedException {
        this.window = window;
        this.name = name;
        synchronized (window.getLock()){
            window.getLock().wait();
        }
    }

    public void pushMessage(PacketSMS packetSMS){
        window.pushMessage(packetSMS);
    }

    public abstract void sendMessage(PacketSMS packetSMS);

    @Override
    public abstract void close();

    public String getName() {
        return name;
    }
}