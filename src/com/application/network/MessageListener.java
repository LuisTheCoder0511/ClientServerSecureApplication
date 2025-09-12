package com.application.network;

public interface MessageListener {
    void onMessageReceived(Client client, String message);
    void onClientDisconnected(Client client);
}
