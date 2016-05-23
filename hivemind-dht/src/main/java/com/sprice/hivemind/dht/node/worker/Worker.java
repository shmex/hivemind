package com.sprice.hivemind.dht.node.worker;

import com.sprice.hivemind.dht.node.Node;
import com.sprice.hivemind.dht.node.NodeEventHandler;

public class Worker extends Node {

    public Worker() {
        super(8, new NodeEventHandler()); //todo expose from properties
    }
}
