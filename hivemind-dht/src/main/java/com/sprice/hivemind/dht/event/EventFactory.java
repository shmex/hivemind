package com.sprice.hivemind.dht.event;

import com.sprice.hivemind.dht.event.exception.UnsupportedEventTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class EventFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EventFactory.class);

    public Event createEvent(byte[] data) throws IOException, UnsupportedEventTypeException {
        int eventType = getIntFromBytes(Arrays.copyOfRange(data, 0, 3));
        LOG.debug("Creating event of type: " + eventType);
        switch(eventType) {
            case EventProtocol.STRING_MESSAGE_EVENT: return new StringMessageEvent(data);
        }

        throw new UnsupportedEventTypeException("Unsupported event type: " + eventType);
    }

    private int getIntFromBytes(byte[] intData) {
        return 0;
    }
}
