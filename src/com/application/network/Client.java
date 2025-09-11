package com.application.network;

import java.io.IOException;
import java.net.Socket;

public class Client extends IOSocket {
    public Client() {
    }

    public Client(Socket socket) throws IOException {
        super.open(socket);
    }

    public void open() throws IOException {
        super.open();
        System.out.println("Joined server!");
    }

    @Override
    public String read() throws IOException {
        return super.read();
    }

    @Override
    public void write(String text) {
        super.write(text);
    }

    public void close() throws IOException {
        super.close();
    }
}
