package com.application.packet;

import java.io.IOException;

public class PacketSMS extends AbstractPacket {
    private final String message;

    public PacketSMS(String message) {
        super();
        this.message = message;
        try {
            size = ObjectToBytesConverter.toBytes(this).length;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public String getMessage() {
        return message;
    }
}