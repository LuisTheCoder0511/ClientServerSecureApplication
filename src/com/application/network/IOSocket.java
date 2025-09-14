package com.application.network;

import com.application.constants.Constants;
import com.application.interfaces.MessageListener;
import com.application.packet.ObjectToBytesConverter;
import com.application.packet.PacketSMS;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;

public abstract class IOSocket {
    protected final Socket socket;
    protected final ObjectOutputStream out;
    protected final ObjectInputStream in;
    protected SecretKey aesKey;
    protected MessageListener listener;
    protected Thread readerThread;

    protected IOSocket(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    protected synchronized void writeObject(Object object) throws IOException {
        out.writeObject(object);
        out.flush();
    }

    protected Object readObject() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    protected void encryptMessage(PacketSMS packet) throws Exception{
        Cipher aesCipher = Cipher.getInstance(Constants.ALG_AES);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] packetBytes = ObjectToBytesConverter.toBytes(packet);
        writeObject(aesCipher.doFinal(packetBytes));
    }

    protected Object decryptMessage(byte[] encrypted) {
        try {
            Cipher aesCipher = Cipher.getInstance(Constants.ALG_AES);
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] packetBytes = aesCipher.doFinal(encrypted);
            return ObjectToBytesConverter.fromBytes(packetBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    protected void close() throws IOException {
        socket.close();
    }
}
