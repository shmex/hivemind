package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;

import java.net.Socket;

public interface Node {
    void onEvent(Event event);
    void onError(String connectionId, Throwable t);
    void onServerError(Throwable t);
    void onIncommingSocketConnect(Socket socket);
}
