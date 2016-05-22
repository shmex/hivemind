package com.sprice.hivemind.dht.transport;

import java.net.Socket;

public class TCPConnectionUtils {

    public String getConnectionId(Socket socket) {
        return socket.getInetAddress() + ":" + socket.getPort();
    }
}
