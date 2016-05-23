package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventProtocol;
import com.sprice.hivemind.dht.transport.TCPConnectionUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SocketEvent extends Event {

    private final TCPConnectionUtils tcpConnectionUtils = TCPConnectionUtils.getInstance();
    private byte[] address;
    private int port;

    public SocketEvent(byte[] address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public int getType() {
        return EventProtocol.SOCKET_EVENT;
    }

    @Override
    protected void deserialize(DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        address = eventUtils.readBytes(dataInputStream);
        port = dataInputStream.readInt();
    }

    @Override
    protected void serialize(DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        eventUtils.writeBytes(address, dataOutputStream);
        dataOutputStream.writeInt(port);
    }
}
