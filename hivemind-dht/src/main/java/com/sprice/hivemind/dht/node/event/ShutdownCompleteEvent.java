package com.sprice.hivemind.dht.node.event;

import com.sprice.hivemind.dht.event.EventProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShutdownCompleteEvent extends ShutdownEvent {

    private long endTimestamp;

    public ShutdownCompleteEvent(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    @Override
    public int getType() {
        return EventProtocol.SHUTDOWN_COMPLETE_EVENT;
    }

    @Override
    protected void deserialize(DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        endTimestamp = dataInputStream.readLong();
    }

    @Override
    protected void serialize(DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        dataOutputStream.writeLong(endTimestamp);
    }
}
