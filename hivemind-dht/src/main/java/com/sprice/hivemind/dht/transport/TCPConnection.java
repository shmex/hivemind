package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.node.Node;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TCPConnection {

    private final TCPConnectionUtils tcpConnectionUtils = TCPConnectionUtils.getInstance();
    private final String connectionId;
    private final TCPSender tcpSender;
    private final TCPReceiver tcpReceiver;
    private final Future receiverFuture;

    public TCPConnection(Node node, Socket socket) throws IOException {
        connectionId = tcpConnectionUtils.getConnectionId(socket);
        tcpSender = new TCPSender(node, socket);
        tcpReceiver = new TCPReceiver(node, socket);
        receiverFuture = node.submitTask(tcpReceiver);
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void send(Event event) throws IOException {
        tcpSender.send(event.getData());
    }

    public void close() throws IOException {
        // receiver closes itself when cancelled
        receiverFuture.cancel(true);
        tcpSender.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TCPConnection that = (TCPConnection) o;

        return connectionId.equals(that.connectionId);

    }

    @Override
    public int hashCode() {
        return connectionId.hashCode();
    }
}
