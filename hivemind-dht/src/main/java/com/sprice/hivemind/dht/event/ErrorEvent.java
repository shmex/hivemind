package com.sprice.hivemind.dht.event;

public class ErrorEvent extends Event {

    private Exception e;

    public ErrorEvent(Exception e) {
        this.e = e;
    }

    public Exception getException() {
        return e;
    }

    @Override
    public int getType() {
        return EventProtocol.ERROR_EVENT;
    }

}
