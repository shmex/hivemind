package com.sprice.hivemind.dht.node.worker;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.node.Node;

public class Worker extends Node {

    public Worker() {
        super(8); //todo expose from properties
    }

    @Override
    public void onEvent(String connectionId, Event event) {
    }

}
