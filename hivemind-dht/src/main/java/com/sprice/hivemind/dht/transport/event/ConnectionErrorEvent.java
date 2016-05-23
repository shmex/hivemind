package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.ErrorEvent;
import com.sprice.hivemind.dht.event.EventProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionErrorEvent extends ErrorEvent {

    private String connectionId;

    public ConnectionErrorEvent(String connectionId, Exception e) {
        super(e);
        this.connectionId = connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public int getType() {
        return EventProtocol.CONNECTION_ERROR_EVENT;
    }

    @Override
    protected void deserialize(DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        connectionId = eventUtils.readString(dataInputStream);
    }

    @Override
    protected void serialize(DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        eventUtils.writeString(connectionId, dataOutputStream);
    }
}
