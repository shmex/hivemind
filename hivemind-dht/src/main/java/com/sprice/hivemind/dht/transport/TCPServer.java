package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.node.Node;

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

    private final Node node;
    private ServerSocket serverSocket;
    private Future serverFuture;

    public TCPServer(Node node) {
        this.node = node;
    }

    public void start(int port) {
        if(serverFuture != null &&
                !(serverFuture.isDone() || serverFuture.isCancelled())) {
            return;
        }
        serverFuture = node.getThreadPool().submit(() -> {
            try {
                listen(port);
            } catch(IOException e) {
                // catch all and report to handler
                node.onServerError(e);
            }
        });
    }

    public void stop() {
        if(serverFuture == null || serverFuture.isDone() || serverFuture.isCancelled()) {
            return;
        }
        serverFuture.cancel(true);
    }

    private void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while(!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            node.onIncomingSocketConnect(socket);
        }
    }
}
