package com.sprice.hivemind.dht.event;

import com.sprice.hivemind.dht.event.exception.UnsupportedEventTypeException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EventFactoryTest {

    private EventFactory eventFactory = EventFactory.getInstance();

    @Test
    public void testCreateStringMessageEvent() throws IOException, UnsupportedEventTypeException {
        String testString = "test message";
        StringMessageEvent stringMessageEvent = new StringMessageEvent(testString);
        assertEquals(stringMessageEvent, eventFactory.createEvent(stringMessageEvent.getData()));
    }

    @Test(expected = UnsupportedEventTypeException.class)
    public void testCreateUnsupportedEventType() throws IOException, UnsupportedEventTypeException {
        byte[] data = {0, 0, 0, 0};
        eventFactory.createEvent(data);
    }

    @Test
    public void testGetIntFromBytes() {
        byte[] data = {0, 0, 0, 1};
        assertEquals(1, eventFactory.getIntFromBytes(data));
        byte[] reverseEndian = {1, 0, 0, 0};
        assertEquals(Math.pow(2, 24), eventFactory.getIntFromBytes(reverseEndian), 0.001);
    }
}
