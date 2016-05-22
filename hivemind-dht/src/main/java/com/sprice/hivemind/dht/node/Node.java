package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.transport.TCPConnection;
import com.sprice.hivemind.dht.transport.TCPConnectionCache;
import com.sprice.hivemind.dht.transport.TCPServer;
import com.sprice.hivemind.dht.transport.exception.ConnectionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Node {

    private static final Logger LOG = LoggerFactory.getLogger(Node.class);
    private final TCPConnectionCache connections;
    private final ExecutorService threadPool;
    private final TCPServer server;

    public Node(int threadPoolSize) {
        this.connections = new TCPConnectionCache();
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.server = new TCPServer(this);
    }

    public abstract void onEvent(String connectionId, Event event);

    public void onError(String connectionId, Throwable t) {
        try {
            connections.remove(connectionId);
            LOG.info("client disconnected: " + connectionId + " because: " + t.getMessage());
        } catch (ConnectionNotFoundException e) {
            LOG.error("onError received nonexistent connectionId: " + connectionId);
        }
    }

    public void onServerError(Throwable t) {
        LOG.info("server stopped because: " + t.getMessage());
    }

    public void onIncomingSocketConnect(Socket socket) {
        try {
            TCPConnection connection = new TCPConnection(this, socket);
            connections.add(connection);
            LOG.info("client connected: " + connection.getConnectionId());
        } catch(IOException e) {
            LOG.error("could not create TCPConnection from socket because: " + e.getMessage());
        }
    }

    public void startServer(int port) {
        LOG.info("starting server on port: " + port);
        this.server.start(port);
    }

    public void stopServer() {
        LOG.info("stopping server");
        this.server.stop();
    }

    public Future submitTask(Runnable task) {
        return threadPool.submit(task);
    }
}
