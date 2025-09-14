package com.application.service;

import com.application.interfaces.MessageListener;
import com.application.network.Client;
import com.application.constants.Constants;
import com.application.input.NetworkInput;
import com.application.packet.PacketSMS;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.*;

public class ServerService extends ServiceClass {
    private final ServerSocket serverSocket;
    private final Queue<Client> clients;

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final Thread thread;

    public ServerService(NetworkInput input) throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(Constants.ALG_RSA);
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        clients = new LinkedList<>();
        serverSocket = new ServerSocket();
        InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(input.getAddress()), input.getPort());
        serverSocket.bind(socketAddress);

        thread = new Thread(this::acceptRequests);
        thread.start();
    }

    private void acceptRequests() {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
            Client client = new Client(socket);
            client.writeObject(publicKey);
            try {
                byte[] encryptedSecretKey = (byte[]) client.readObject();

                Cipher rsaCipher = Cipher.getInstance(Constants.ALG_RSA);
                rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] aesKeyBytes = rsaCipher.doFinal(encryptedSecretKey);

                client.setAESKey(new SecretKeySpec(aesKeyBytes, Constants.ALG_AES));
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            client.setMessageListener(new MessageListener() {
                @Override
                public void onMessageReceived(Client client, PacketSMS packetSMS) {
                    window.pushMessage(packetSMS);
                    for (Client other: clients) {
                        if (other == client) continue;
                        try {
                            other.sendMessage(packetSMS);
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
            if (window.isOpen()) acceptRequests();
        } catch (IOException e){
            System.err.println("Server Closed");
        }
    }

    @Override
    public void sendMessage(PacketSMS packetSMS) {
        try {
            for (Client client : clients) {
                client.sendMessage(packetSMS);
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
                if (client.isClosed()) continue;
                client.close();
            }
            serverSocket.close();
            thread.interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}