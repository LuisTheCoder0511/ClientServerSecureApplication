package com.application.network;

import java.io.*;
import java.net.Socket;

public abstract class IOSocket {
    protected Socket socket;
    protected PrintWriter printWriter;
    protected BufferedReader bufferedReader;

    protected void open() throws IOException {
        open(new Socket(Constants.IP, Constants.PORT));
    }

    protected void open(Socket socket) throws IOException {
        this.socket = socket;
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    protected void close() throws IOException {
        printWriter.close();
        bufferedReader.close();
        socket.close();
    }
}
