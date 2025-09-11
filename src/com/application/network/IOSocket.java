package com.application.network;

import com.application.window.Window;

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

    protected String read() throws IOException {
        String line = bufferedReader.readLine();
        if (!line.isBlank()){
            System.out.println("In: " + line);
            return line;
        }
        return null;
    }

    protected void write(String text){
        printWriter.println(text.isBlank() ? "" : text);
    }

    protected void close() throws IOException {
        printWriter.close();
        bufferedReader.close();
        socket.close();
    }

    public String getSocketAddress() {
        return socket.getInetAddress().getHostAddress();
    }
}
