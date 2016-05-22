package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.node.Node;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSender {

    private final Node node;
    private final DataOutputStream dataOutputStream;

    public TCPSender(Node node, Socket socket) throws IOException {
        this.node = node;
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void send(byte[] data) throws IOException {
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data, 0, data.length);
        dataOutputStream.flush();
    }

    public void close() throws IOException {
        dataOutputStream.close();
    }
}
