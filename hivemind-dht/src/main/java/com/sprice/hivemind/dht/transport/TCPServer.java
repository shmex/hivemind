package com.sprice.hivemind.dht.transport;

import com.sprice.hivemind.dht.node.Node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

    private final Node node;
    private final int port;
    private ServerSocket serverSocket;

    public TCPServer(Node node, int port) {
        this.node = node;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            listen();
        } catch(IOException e) {
            // catch all and report to handler
            node.onServerError(e);
        }
    }

    private void listen() throws IOException {
        serverSocket = new ServerSocket(port);
        while(!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            node.onIncommingSocketConnect(socket);
        }
    }
}
