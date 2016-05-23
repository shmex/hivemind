package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.EventProtocol;

import java.net.Socket;

public class OutgoingConnectionEvent extends SocketEvent {

    private Socket socket;

    public OutgoingConnectionEvent(Socket socket) {
        super(socket.getInetAddress().getAddress(), socket.getPort());
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public int getType() {
        return EventProtocol.OUTGOING_CONNECTION_EVENT;
    }

    // do not override SocketEvent's serializer/deserializer -- if this gets serialized
    // socket should not be serialized with the connection info
}
