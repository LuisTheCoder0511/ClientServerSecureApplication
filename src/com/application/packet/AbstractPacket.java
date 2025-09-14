package com.application.packet;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AbstractPacket implements Serializable {
    protected final LocalDateTime dateTimeSent;
    protected long size;

    protected AbstractPacket() {
        dateTimeSent = LocalDateTime.now();
    }

    public LocalDateTime getDateTimeSent() {
        return dateTimeSent;
    }

    public long getSize() {
        return size;
    }
}
