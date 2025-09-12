package com.application.network;

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

    protected IOSocket() throws IOException {
        this(new Socket(Constants.IP, Constants.PORT));
    }

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

    protected void encryptMessage(String message) throws Exception{
        Cipher aesCipher = Cipher.getInstance(Constants.ALG_AES);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        writeObject(aesCipher.doFinal(message.getBytes()));
    }

    protected String decryptMessage(byte[] encrypted) {
        try {
            Cipher aesCipher = Cipher.getInstance(Constants.ALG_AES);
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
            return new String(aesCipher.doFinal(encrypted));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    protected void close() throws IOException {
        socket.close();
    }
}
