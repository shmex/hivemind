package com.sprice.hivemind.dht.node.event;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventProtocol;

public class ShutdownEvent extends Event {
    @Override
    public int getType() {
        return EventProtocol.SHUTDOWN_EVENT;
    }
}
