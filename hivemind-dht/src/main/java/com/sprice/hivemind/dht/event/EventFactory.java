package com.sprice.hivemind.dht.event;

import com.sprice.hivemind.dht.event.exception.UnsupportedEventTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class EventFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EventFactory.class);
    private static final EventFactory instance = new EventFactory();

    private EventFactory() {}

    public static EventFactory getInstance() {
        return instance;
    }

    public Event createEvent(byte[] data) throws IOException, UnsupportedEventTypeException {
        int eventType = getIntFromBytes(Arrays.copyOfRange(data, 0, 4));
        switch(eventType) {
            case EventProtocol.STRING_MESSAGE_EVENT: return new StringMessageEvent(data);
        }

        throw new UnsupportedEventTypeException("Unsupported EventType: " + eventType);
    }

    protected int getIntFromBytes(byte[] intData) {
        return   intData[3] & 0xFF |
                (intData[2] & 0xFF) << 8 |
                (intData[1] & 0xFF) << 16 |
                (intData[0] & 0xFF) << 24;
    }
}
