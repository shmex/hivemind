package com.sprice.hivemind.dht.node.event;

import com.sprice.hivemind.dht.event.EventProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShutdownStartEvent extends ShutdownEvent {

    long startTimestamp;

    public ShutdownStartEvent(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    @Override
    public int getType() {
        return EventProtocol.SHUTDOWN_START_EVENT;
    }

    @Override
    protected void deserialize(DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        startTimestamp = dataInputStream.readLong();
    }

    @Override
    protected void serialize(DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        dataOutputStream.writeLong(startTimestamp);
    }
}
