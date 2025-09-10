package com.application.network;

import java.io.*;
import java.net.*;

public class ClientHandler extends IOSocket {
    public ClientHandler(Socket socket) throws IOException {
        super.open(socket);
    }

    public void listen(String text) throws IOException {
        System.out.println("In: " + bufferedReader.readLine());
        if (text != null){
            printWriter.println(text);
            System.out.println("Out: " + text);
        }
    }

    public void close() throws IOException {
        super.close();
    }
}