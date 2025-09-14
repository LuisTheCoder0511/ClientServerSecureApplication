package com.application.service;

import com.application.interfaces.MessageListener;
import com.application.network.Client;
import com.application.constants.Constants;
import com.application.input.NetworkInput;
import com.application.packet.PacketSMS;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.net.Socket;
import java.security.PublicKey;

public class ClientService extends ServiceClass {
    private final Client client;

    public ClientService(NetworkInput input) {
        try {
            client = new Client(new Socket(input.getAddress(), input.getPort()));
            PublicKey publicKey = (PublicKey) client.readObject();
            KeyGenerator keyGen = KeyGenerator.getInstance(Constants.ALG_AES);
            keyGen.init(128);
            client.setAESKey(keyGen.generateKey());

            Cipher rsaCipher = Cipher.getInstance(Constants.ALG_RSA);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedAESKey = rsaCipher.doFinal(client.getAESKey().getEncoded());

            client.writeObject(encryptedAESKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.setMessageListener(new MessageListener() {
            @Override
            public void onMessageReceived(Client client, PacketSMS packetSMS) {
                pushMessage(packetSMS);
            }

            @Override
            public void onClientDisconnected(Client client) {
                System.out.println("Server disconnected");
            }
        });
        client.startListening();
    }

    @Override
    public void sendMessage(PacketSMS packetSMS) {
        try {
            client.sendMessage(packetSMS);
        } catch (Exception e) {
            close();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}