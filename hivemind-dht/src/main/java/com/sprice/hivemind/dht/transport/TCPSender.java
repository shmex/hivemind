package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.node.Node;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSender {

    private final Node node;
    private final Socket socket;

    public TCPSender(Node node, Socket socket) {
        this.node = node;
        this.socket = socket;
    }

    public void send(byte[] data) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data, 0, data.length);
        dataOutputStream.flush();
        dataOutputStream.close();
    }
}
