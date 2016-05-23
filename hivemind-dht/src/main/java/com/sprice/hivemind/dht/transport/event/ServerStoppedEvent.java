package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventProtocol;

public class ServerStoppedEvent extends Event {

    @Override
    public int getType() {
        return EventProtocol.SERVER_STOPPED_EVENT;
    }

}
