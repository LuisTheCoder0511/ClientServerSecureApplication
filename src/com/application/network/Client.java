package com.application.network;

import com.application.interfaces.MessageListener;
import com.application.packet.PacketSMS;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;

public class Client extends IOSocket {
    public Client(Socket socket) throws IOException {
        super(socket);
    }

    public void startListening() {
        readerThread = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    Object object = in.readObject();
                    if (!(object instanceof byte[] encrypted)) continue;
                    PacketSMS packetSMS = readMessage(encrypted);
                    if (listener != null) {
                        listener.onMessageReceived(this, packetSMS);
                    }
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onClientDisconnected(this);
                }
                throw new RuntimeException(e);
            }
        });
        readerThread.start();
    }

    public void sendMessage(PacketSMS packetSMS) throws Exception {
        encryptMessage(packetSMS);
    }

    private PacketSMS readMessage(byte[] encrypted) {
        PacketSMS packetSMS = (PacketSMS) decryptMessage(encrypted);
        System.out.println("Packet size: " + packetSMS.getSize());
        return packetSMS;
    }

    @Override
    public synchronized void writeObject(Object object) throws IOException {
        super.writeObject(object);
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return super.readObject();
    }

    public SecretKey getAESKey(){
        return aesKey;
    }

    public void setAESKey(SecretKey key){
        aesKey = key;
    }

    public void setMessageListener(MessageListener listener) {
        super.setMessageListener(listener);
    }

    public void close() throws IOException {
        super.close();
    }
}
