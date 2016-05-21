package com.sprice.hivemind.dht.transport;

import java.net.Socket;

public class TCPSender {

    private final Socket socket;

    public TCPSender(Socket socket) {
        this.socket = socket;
    }

    public void send(byte[] data) {
    }
}
