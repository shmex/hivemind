package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.event.EventFactory;
import com.sprice.hivemind.dht.event.exception.UnsupportedEventTypeException;
import com.sprice.hivemind.dht.node.Node;
import com.sprice.hivemind.dht.transport.event.ConnectionErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPReceiver implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(TCPReceiver.class);
    private final TCPConnectionUtils tcpConnectionUtils = TCPConnectionUtils.getInstance();
    private final EventFactory eventFactory = EventFactory.getInstance();
    private final Node node;
    private final Socket socket;
    private final DataInputStream dataInputStream;

    public TCPReceiver(Node node, Socket socket) throws IOException {
        this.node = node;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            receive();
        } catch(Exception e) {
            // catch all, report to other thread
            String connectionId = tcpConnectionUtils.getConnectionId(socket);
            node.onEvent(new ConnectionErrorEvent(connectionId, e));
        } finally {
            close();
        }
    }

    private void close() {
        try {
            dataInputStream.close();
        } catch(IOException e) {
            LOG.error("failed to close receiver's DataInputStream because: " + e.getMessage());
        }
    }

    private void receive() throws IOException, UnsupportedEventTypeException {
        while(!Thread.currentThread().isInterrupted()) {
            int dataLength = dataInputStream.readInt();
            byte[] data = new byte[dataLength];
            dataInputStream.readFully(data, 0, dataLength);
            node.onEvent(eventFactory.createEvent(data)); // todo add connection details
        }
    }

}
