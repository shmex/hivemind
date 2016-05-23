package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerStartedEvent extends Event {

    private int port;

    public ServerStartedEvent(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int getType() {
        return EventProtocol.SERVER_STARTED_EVENT;
    }

    @Override
    protected void deserialize(DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        port = dataInputStream.readInt();
    }

    @Override
    protected void serialize(DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        dataOutputStream.writeInt(port);
    }
}
