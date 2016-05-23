package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.ErrorEvent;
import com.sprice.hivemind.dht.event.EventProtocol;

public class ServerErrorEvent extends ErrorEvent {

    public ServerErrorEvent(Exception e) {
        super(e);
    }

    @Override
    public int getType() {
        return EventProtocol.SERVER_ERROR_EVENT;
    }
}
