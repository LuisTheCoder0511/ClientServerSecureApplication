package com.application.network;

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
                    String message = decryptMessage(encrypted);
                    if (listener != null) {
                        listener.onMessageReceived(this, message);
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

    public void sendMessage(String message) throws Exception {
        encryptMessage(message);
    }

    @Override
    protected synchronized void writeObject(Object object) throws IOException {
        super.writeObject(object);
    }

    @Override
    protected Object readObject() throws IOException, ClassNotFoundException {
        return super.readObject();
    }

    public void close() throws IOException {
        super.close();
    }
}
