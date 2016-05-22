package com.sprice.hivemind.dht.transport;


import com.sprice.hivemind.dht.transport.exception.ConnectionNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TCPConnectionCache {

    private Map<String, TCPConnection> connections;

    public TCPConnectionCache() {
        connections = new HashMap<>();
    }

    public TCPConnection get(String connectionId) throws ConnectionNotFoundException {
        synchronized (connections) {
            validateContains(connectionId);
            return connections.get(connectionId);
        }
    }

    public void add(TCPConnection tcpConnection) {
        synchronized (connections) {
            connections.put(tcpConnection.getConnectionId(), tcpConnection);
        }
    }

    public TCPConnection remove(String connectionId) throws ConnectionNotFoundException {
        synchronized (connections) {
            validateContains(connectionId);
            return connections.remove(connectionId);
        }
    }

    public Set<TCPConnection> getAll() {
        synchronized (connections) {
            return new HashSet<>(connections.values());
        }
    }

    private void validateContains(String connectionId) throws ConnectionNotFoundException {
        synchronized (connections) {
            if (!connections.containsKey(connectionId)) {
                throw new ConnectionNotFoundException("Could not find connection with connectionId: " + connectionId);
            }
        }
    }
}
