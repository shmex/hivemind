package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;

public interface Node {
    void onEvent(Event event);
    void onError(Throwable t);
}
