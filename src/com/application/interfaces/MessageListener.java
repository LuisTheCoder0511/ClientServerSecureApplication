package com.application.interfaces;

import com.application.network.Client;

public interface MessageListener {
    void onMessageReceived(Client client, String message);
    void onClientDisconnected(Client client);
}
