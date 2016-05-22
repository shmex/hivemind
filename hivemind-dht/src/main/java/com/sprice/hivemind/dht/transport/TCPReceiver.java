package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.event.EventFactory;
import com.sprice.hivemind.dht.event.exception.UnsupportedEventTypeException;
import com.sprice.hivemind.dht.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPReceiver implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(TCPReceiver.class);
    private final Node node;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final EventFactory eventFactory = EventFactory.getInstance();
    private final TCPConnectionUtils tcpConnectionUtils;

    public TCPReceiver(Node node, Socket socket) throws IOException {
        this.node = node;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.tcpConnectionUtils = new TCPConnectionUtils();
    }

    @Override
    public void run() {
        try {
            receive();
        } catch(Exception e) {
            // catch all, report to other thread
            node.onConnectionError(tcpConnectionUtils.getConnectionId(socket), e);
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
            node.onEvent(tcpConnectionUtils.getConnectionId(socket), eventFactory.createEvent(data));
        }
    }

}
