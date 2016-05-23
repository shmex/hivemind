package com.sprice.hivemind.dht.event;

import java.util.LinkedList;
import java.util.Queue;

public class EventHandlerRegistrar {

    private Queue<EventHandler> eventHandlers;

    public EventHandlerRegistrar() {
        eventHandlers = new LinkedList<>();
    }

    public void registerEventHandler(EventHandler eventHandler) {
        synchronized (eventHandlers) {
            eventHandlers.add(eventHandler);
        }
    }

    public void unregisterEventHandler(EventHandler eventHandler) {
        synchronized (eventHandlers) {
            eventHandlers.remove(eventHandler);
        }
    }

    public Queue<EventHandler> getAll() {
        synchronized (eventHandlers) {
            return new LinkedList<>(eventHandlers);
        }
    }
}
