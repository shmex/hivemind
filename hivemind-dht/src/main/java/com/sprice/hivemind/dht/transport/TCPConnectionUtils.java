package com.sprice.hivemind.dht.transport;

import java.net.Socket;

public class TCPConnectionUtils {

    private static final TCPConnectionUtils instance = new TCPConnectionUtils();

    private TCPConnectionUtils() {}

    public static TCPConnectionUtils getInstance() {
        return instance;
    }

    public String getConnectionId(Socket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
}
