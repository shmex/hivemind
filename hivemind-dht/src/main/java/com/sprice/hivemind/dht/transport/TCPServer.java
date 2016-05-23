package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.event.EventFactory;
import com.sprice.hivemind.dht.node.Node;
import com.sprice.hivemind.dht.transport.event.IncomingConnectionEvent;
import com.sprice.hivemind.dht.transport.event.ServerErrorEvent;
import com.sprice.hivemind.dht.transport.event.ServerStartedEvent;
import com.sprice.hivemind.dht.transport.event.ServerStoppedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Future;

/**
 * Self managed, restartable server
 * Registers against a node
 *
 * Note: the server thread performs the
 * node's onIncomingSocketConnect work.
 * This means the server will not actively
 * listen until that work is done.
 */
public class TCPServer {

    private static final Logger LOG = LoggerFactory.getLogger(TCPServer.class);
    private final TCPConnectionUtils tcpConnectionUtils = TCPConnectionUtils.getInstance();
    private final EventFactory eventFactory = EventFactory.getInstance();
    private final Node node;
    private ServerSocket serverSocket;
    private Future serverFuture;

    public TCPServer(Node node) {
        this.node = node;
    }

    public void start(int port) {
        if(isRunning()) {
            return;
        }
        serverFuture = node.submitTask(() -> {
            try {
                listen(port);
            } catch(IOException e) {
                // catch all and report to handler
                node.onEvent(new ServerErrorEvent(e));
            }
        });
    }

    public void stop() throws IOException {
        if(isRunning()) {
            serverSocket.close();
            node.onEvent(new ServerStoppedEvent());
        }
    }

    public boolean isRunning() {
        if(serverFuture == null) return false;
        return !serverFuture.isCancelled() && !serverFuture.isDone();
    }

    private void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        node.onEvent(new ServerStartedEvent(port));
        while(!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            node.onEvent(new IncomingConnectionEvent(socket));
        }
    }
}
