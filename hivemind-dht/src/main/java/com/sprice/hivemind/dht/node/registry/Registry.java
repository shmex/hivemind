package com.sprice.hivemind.dht.node.registry;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventProtocol;
import com.sprice.hivemind.dht.event.StringMessageEvent;
import com.sprice.hivemind.dht.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Registry extends Node {

    private static final Logger LOG = LoggerFactory.getLogger(Registry.class);

    public Registry() {
        super(8); // todo get thread pool size and port from properties file
    }

    @Override
    public void onEvent(String connectionId, Event event) {
        switch(event.getType()) {
            case EventProtocol.STRING_MESSAGE_EVENT:
                handleStringMessageEvent((StringMessageEvent) event);
                break;
        }
    }

    private void handleStringMessageEvent(StringMessageEvent event) {
        LOG.info("[STRING_MESSAGE_EVENT]: " + event.getMessage());
    }

}
