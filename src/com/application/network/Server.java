package com.application.network;

import com.application.window.Window;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    public final ServerSocket serverSocket = new ServerSocket(Constants.PORT);
    private final ScheduledExecutorService readingService;
    private final ScheduledExecutorService writingService;
    private final Queue<Client> clients;
    private final Queue<String> clientMessages;
    private final Queue<Client> removeClientQueue;

    public Server(Window window) throws IOException {
        clients = new LinkedList<>();
        clientMessages = new LinkedList<>();
        removeClientQueue = new LinkedList<>();
        readingService = Executors.newScheduledThreadPool(1);
        writingService = Executors.newScheduledThreadPool(1);

        readingService.scheduleAtFixedRate(() -> {
            for (Client client: clients) {
                try {
                    if (client.socket.isClosed()){
                        removeClientQueue.add(client);
                        continue;
                    }
                    String line = client.read();
                    if (line != null){
                        clientMessages.add(line);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Client clientToRemove;
            while ((clientToRemove = removeClientQueue.poll()) != null){
                clients.remove(clientToRemove);
            }
            System.out.println(Arrays.toString(clients.toArray()));
        }, 0, 250, TimeUnit.MILLISECONDS);

        writingService.scheduleAtFixedRate(() -> {
            if (!window.getText().isBlank()){
                clientMessages.add("Server: " + window.getText());
                window.resetText();
            }
            String line;
            while ((line = clientMessages.poll()) != null){
                for (Client client: clients) {
                    if (client.socket.isClosed()) continue;
                    client.write(line);
                }
                System.out.println("Out: " + line);
                window.pushMessage(line);
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    public void acceptRequests(Window window) throws IOException {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
            clients.add(new Client(socket));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (window.isClosed()) close();
        else acceptRequests(window);
    }

    public void close() throws IOException {
        readingService.shutdownNow();
        writingService.shutdownNow();
        for (Client client: clients) {
            if (client.socket.isClosed()) continue;
            client.close();
        }
        clients.clear();
        clientMessages.clear();
        removeClientQueue.clear();
        serverSocket.close();
    }
}
