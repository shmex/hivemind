package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventHandler;
import com.sprice.hivemind.dht.event.EventHandlerRegistrar;
import com.sprice.hivemind.dht.node.event.SubmitTaskEvent;
import com.sprice.hivemind.dht.transport.TCPConnection;
import com.sprice.hivemind.dht.transport.TCPConnectionCache;
import com.sprice.hivemind.dht.transport.TCPServer;
import com.sprice.hivemind.dht.transport.event.OutgoingConnectionEvent;
import com.sprice.hivemind.dht.transport.event.SendEvent;
import com.sprice.hivemind.dht.node.event.ShutdownCompleteEvent;
import com.sprice.hivemind.dht.node.event.ShutdownStartEvent;
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
public abstract class Node implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(Node.class);
    protected final TCPConnectionCache connections;
    private final EventHandlerRegistrar eventHandlerRegistrar;
    private final ExecutorService threadPool;
    private final TCPServer server;

    public Node(int threadPoolSize, NodeEventHandler nodeEventHandler) {
        this.connections = new TCPConnectionCache();
        this.eventHandlerRegistrar = new EventHandlerRegistrar();
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.server = new TCPServer(this);

        nodeEventHandler.registerNode(this);
        this.eventHandlerRegistrar.registerEventHandler(nodeEventHandler);
    }

    public void onEvent(Event event) {
        for(EventHandler eventHandler : eventHandlerRegistrar.getAll()) {
            eventHandler.onEvent(event);
        }
    }

    public void startServer(int port) {
        if(!server.isRunning()) {
            this.server.start(port);
        }
    }

    public void stopServer() {
        if(server.isRunning()) {
            try {
                this.server.stop();
            } catch(IOException e) {
                LOG.error("failed to stop server: " + e.getMessage());
            }
        }
    }

    public void connect(InetAddress inetAddress, int port) throws IOException {
        Socket socket = new Socket(inetAddress, port);
        this.onEvent(new OutgoingConnectionEvent(socket));
    }

    public void send(String connectionId, Event event) throws ConnectionNotFoundException, IOException {
        connections.get(connectionId).send(event);
        this.onEvent(new SendEvent(event));
    }

    public void shutdown() {
        this.onEvent(new ShutdownStartEvent(System.currentTimeMillis()));
        stopServer();
        closeAllConnections();
        shutdownThreadPool();
        this.onEvent(new ShutdownCompleteEvent(System.currentTimeMillis()));
    }

    public Future submitTask(Runnable task) {
        Future future = threadPool.submit(task);
        this.onEvent(new SubmitTaskEvent(task, future));
        return future;
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
