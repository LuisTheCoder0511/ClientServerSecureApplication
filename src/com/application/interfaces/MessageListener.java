package com.application.interfaces;

import com.application.network.Client;
import com.application.packet.PacketSMS;

public interface MessageListener {
    void onMessageReceived(Client client, PacketSMS packetSMS);
    void onClientDisconnected(Client client);
}
