package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.EventProtocol;

import java.net.Socket;

public class IncomingConnectionEvent extends SocketEvent {

    private Socket socket;

    public IncomingConnectionEvent(Socket socket) {
        super(socket.getInetAddress().getAddress(), socket.getLocalPort());
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public int getType() {
        return EventProtocol.INCOMING_CONNECTION_EVENT;
    }

    // do not override SocketEvent's serializer/deserializer -- if this gets serialized
    // socket should not be serialized with the connection info
}
