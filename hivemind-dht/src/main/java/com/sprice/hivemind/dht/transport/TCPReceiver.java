package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.node.Node;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPReceiver implements Runnable {

    private final Node node;
    private final Socket socket;
    private final DataInputStream dataInputStream;

    public TCPReceiver(Node node, Socket socket) throws IOException {
        this.node = node;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public void run() {

    }

    public void receive() throws IOException {
        int dataLength = dataInputStream.readInt();
        byte[] data = new byte[dataLength];
        dataInputStream.readFully(data, 0, dataLength);

    }
}
