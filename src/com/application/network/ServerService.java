package com.application.network;

import com.application.service.ServiceClass;
import com.application.window.Window;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.*;

public class ServerService extends ServiceClass {
    private final ServerSocket serverSocket = new ServerSocket(Constants.PORT);
    private final Queue<Client> clients;

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final Thread thread;

    public ServerService(Window window) throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(Constants.ALG_RSA);
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        clients = new LinkedList<>();

        thread = new Thread(() -> {
            try {
                acceptRequests(window);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    private void acceptRequests(Window window) throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
        Client client = new Client(socket);
        client.writeObject(publicKey);
        try {
            byte[] encryptedSecretKey = (byte[]) client.readObject();

            Cipher rsaCipher = Cipher.getInstance(Constants.ALG_RSA);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] aesKeyBytes = rsaCipher.doFinal(encryptedSecretKey);

            client.aesKey = new SecretKeySpec(aesKeyBytes, Constants.ALG_AES);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        client.setMessageListener(new MessageListener() {
            @Override
            public void onMessageReceived(Client client, String message) {
                window.pushMessage(message);
                for (Client other: clients) {
                    if (other == client) continue;
                    try {
                        other.sendMessage(message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onClientDisconnected(Client client) {
                System.out.println("Client disconnected: " + client);
                clients.remove(client);
            }
        });
        client.startListening();
        clients.add(client);

        acceptRequests(window);
    }

    @Override
    public void sendMessage(String message) {
        try {
            for (Client client : clients) {
                client.sendMessage(message);
            }
        } catch (Exception e) {
            close();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            for (Client client : clients) {
                if (client.socket.isClosed()) continue;
                client.close();
            }
            serverSocket.close();
            thread.interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}