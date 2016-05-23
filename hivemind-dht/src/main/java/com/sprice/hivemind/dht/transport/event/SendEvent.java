package com.sprice.hivemind.dht.transport.event;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventFactory;
import com.sprice.hivemind.dht.event.EventProtocol;
import com.sprice.hivemind.dht.event.exception.UnsupportedEventTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SendEvent extends Event {

    private static final Logger LOG = LoggerFactory.getLogger(SendEvent.class);
    private final EventFactory eventFactory = EventFactory.getInstance();
    public Event sentEvent;

    public SendEvent(Event sentEvent) {
        this.sentEvent = sentEvent;
    }

    public Event getSentEvent() {
        return sentEvent;
    }

    @Override
    public int getType() {
        return EventProtocol.SEND_EVENT;
    }

    @Override
    protected void deserialize(DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        int  eventBytesLen = dataInputStream.readInt();
        byte[] eventBytes = new byte[eventBytesLen];
        dataInputStream.readFully(eventBytes, 0, eventBytesLen);
        try {
            sentEvent = eventFactory.createEvent(eventBytes);
        } catch (UnsupportedEventTypeException e) {
            LOG.error("SendEvent deserialization failed: " + e.getMessage());
        }
    }

    @Override
    protected void serialize(DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        byte[] eventBytes = sentEvent.getData();
        dataOutputStream.writeInt(eventBytes.length);
        dataOutputStream.write(eventBytes, 0, eventBytes.length);
    }
}
