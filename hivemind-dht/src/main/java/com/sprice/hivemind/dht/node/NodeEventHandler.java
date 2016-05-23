package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventHandler;
import com.sprice.hivemind.dht.event.EventProtocol;
import com.sprice.hivemind.dht.node.event.ShutdownCompleteEvent;
import com.sprice.hivemind.dht.node.event.ShutdownStartEvent;
import com.sprice.hivemind.dht.transport.TCPConnection;
import com.sprice.hivemind.dht.transport.event.*;
import com.sprice.hivemind.dht.transport.exception.ConnectionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NodeEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NodeEventHandler.class);
    protected Node node;

    public void registerNode(Node node) {
        this.node = node;
    }

    @Override
    public void onEvent(Event event) {
        switch(event.getType()) {
            case EventProtocol.INCOMING_CONNECTION_EVENT:
                onIncomingConnectionEvent((IncomingConnectionEvent)event);
                break;
            case EventProtocol.CONNECTION_ERROR_EVENT:
                onConnectionErrorEvent((ConnectionErrorEvent)event);
                break;
            case EventProtocol.SERVER_ERROR_EVENT:
                onServerErrorEvent((ServerErrorEvent)event);
                break;
            case EventProtocol.SERVER_STARTED_EVENT:
                onServerStartedEvent((ServerStartedEvent)event);
                break;
            case EventProtocol.OUTGOING_CONNECTION_EVENT:
                onOutgoingConnectionEvent((OutgoingConnectionEvent)event);
                break;
            case EventProtocol.SHUTDOWN_START_EVENT:
                onShutdownStartEvent((ShutdownStartEvent)event);
                break;
            case EventProtocol.SHUTDOWN_COMPLETE_EVENT:
                onShutdownCompleteEvent((ShutdownCompleteEvent)event);
                break;
            case EventProtocol.SEND_EVENT:
                onSendEvent((SendEvent)event);
                break;
        }
    }

    protected void onIncomingConnectionEvent(IncomingConnectionEvent event) {
        try {
            TCPConnection connection = new TCPConnection(node, event.getSocket());
            node.connections.add(connection);
            LOG.info("client connected: " + connection.getConnectionId());
        } catch(IOException e) {
            LOG.error("could not create TCPConnection from socket because: " + e.getMessage());
        }
    }

    protected void onConnectionErrorEvent(ConnectionErrorEvent event) {
        try {
            node.connections.remove(event.getConnectionId());
            LOG.info("client disconnected: " + event.getConnectionId() + " because: " + event.getException().getMessage());
        } catch (ConnectionNotFoundException e) {
            LOG.error("onConnectionError received nonexistent connectionId: " + event.getConnectionId());
        }
    }

    protected void onServerErrorEvent(ServerErrorEvent event) {
        LOG.info("server stopped: " + event.getException().getMessage());
    }

    protected void onServerStartedEvent(ServerStartedEvent event) {
        LOG.info("server started on port: " + event.getPort());
    }

    protected void onOutgoingConnectionEvent(OutgoingConnectionEvent event) {
        try {
            node.connections.add(new TCPConnection(node, event.getSocket()));
        } catch (IOException e) {
            LOG.error("failed to create outgoing TCPConnection: " + e.getMessage());
        }
    }

    protected void onShutdownStartEvent(ShutdownStartEvent event) {
        LOG.info("shutdown initiated at: " + event.getStartTimestamp());
    }

    protected void onShutdownCompleteEvent(ShutdownCompleteEvent event) {
        LOG.info("shutdown completed at: " + event.getEndTimestamp());
    }

    protected void onSendEvent(SendEvent event) {
    }
}
