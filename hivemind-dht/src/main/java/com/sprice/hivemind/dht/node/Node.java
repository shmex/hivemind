package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.transport.TCPConnection;
import com.sprice.hivemind.dht.transport.TCPConnectionCache;
import com.sprice.hivemind.dht.transport.TCPServer;
import com.sprice.hivemind.dht.transport.exception.ConnectionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Manages incoming and outgoing connections
 * also performs event routing
 */
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

    public void onConnectionError(String connectionId, Throwable t) {
        try {
            connections.remove(connectionId);
            LOG.info("client disconnected: " + connectionId + " because: " + t.getMessage());
        } catch (ConnectionNotFoundException e) {
            LOG.error("onConnectionError received nonexistent connectionId: " + connectionId);
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
        if(!server.isRunning()) {
            LOG.info("starting server on port: " + port);
            this.server.start(port);
        }
    }

    public void stopServer() {
        if(server.isRunning()) {
            LOG.info("stopping server");
            try {
                this.server.stop();
            } catch(IOException e) {
                LOG.error("failed to stop server: " + e.getMessage());
            }
        }
    }

    public TCPConnection connect(InetAddress inetAddress, int port) throws IOException {
        Socket socket = new Socket(inetAddress, port);
        TCPConnection connection = new TCPConnection(this, socket);
        connections.add(connection);
        return connection;
    }

    public void send(String connectionId, Event event) throws ConnectionNotFoundException, IOException {
        connections.get(connectionId).send(event);
    }

    public void shutdown() {
        LOG.info("shutting down...");
        stopServer();
        closeAllConnections();
        shutdownThreadPool();
        LOG.info("shutdown " + (threadPool.isShutdown() ? "complete" : "failed") + ".");
    }

    public Future submitTask(Runnable task) {
        return threadPool.submit(task);
    }

    private void closeAllConnections() {
        for(TCPConnection connection : connections.getAll()) {
            try {
                connection.close();
                connections.remove(connection.getConnectionId());
            } catch (Exception e) {
                LOG.error("during shutdown, connection close failed: " + e.getMessage());
            }
        }
    }

    private void shutdownThreadPool() {
        threadPool.shutdown();
        try {
            // give a few seconds for event sending tasks to complete
            if(!threadPool.awaitTermination(3, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                // give the running tasks time to respond to termination
                if(!threadPool.awaitTermination(3, TimeUnit.SECONDS)) {
                    LOG.error("failed to terminate thread pool!");
                }
            }
        } catch (InterruptedException e) {
            // (Re-)Cancel if current thread also interrupted
            threadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
