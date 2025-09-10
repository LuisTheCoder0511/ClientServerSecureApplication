package com.application.network;

import com.application.window.Window;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    public final ServerSocket serverSocket = new ServerSocket(Constants.PORT);
    public boolean closed = false;
    public final ScheduledExecutorService service;
    private final Map<String, ClientHandler> clients;
    public String text;

    public Server() throws IOException {
        clients = new HashMap<>();
        service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            for (String key: clients.keySet()) {
                try {
                    clients.get(key).listen(text);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    public void acceptRequests(Window window) throws IOException {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
            clients.put(socket.getInetAddress().getHostAddress(), new ClientHandler(socket));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (window.isClosed()) acceptRequests(window);
            else close();
        }
    }

    public void close() throws IOException {
        service.shutdownNow();
        for (String key: clients.keySet()) {
            clients.get(key).close();
        }
        serverSocket.close();
        closed = true;
    }
}
