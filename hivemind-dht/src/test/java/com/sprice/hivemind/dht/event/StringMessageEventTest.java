package com.sprice.hivemind.dht.event;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringMessageEventTest {

    private byte[] generateByteArray(int eventType, String message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeInt(eventType);
            dataOutputStream.writeInt(message.length());
            dataOutputStream.write(message.getBytes(), 0, message.length());
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } finally {
            dataOutputStream.close();
        }
    }

    @Test
    public void testCreateFromValidByteArray() throws IOException {
        String testString = "test message";
        byte[] data = generateByteArray(EventProtocol.STRING_MESSAGE_EVENT, testString);
        StringMessageEvent expectedEvent = new StringMessageEvent(testString);
        assertEquals(expectedEvent, new StringMessageEvent(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithIncorrectEventType() throws IOException {
        String testString = "test message";
        byte[] data = generateByteArray(0, testString);
        new StringMessageEvent(data);
    }

    @Test
    public void testSerialize() throws IOException {
        String testString = "test message";
        StringMessageEvent stringMessageEvent = new StringMessageEvent(testString);
        byte[] expectedBytes = generateByteArray(EventProtocol.STRING_MESSAGE_EVENT, testString);
        assertTrue(Arrays.equals(expectedBytes, stringMessageEvent.getData()));
    }

    @Test
    public void testGetType() {
        assertEquals(EventProtocol.STRING_MESSAGE_EVENT, new StringMessageEvent("bogus").getType());
    }

    @Test
    public void testGetMessage() throws IOException {
        String testString = "test message";
        byte[] data = generateByteArray(EventProtocol.STRING_MESSAGE_EVENT, testString);
        StringMessageEvent stringMessageEvent = new StringMessageEvent(data);
        assertEquals(testString, stringMessageEvent.getMessage());
    }

    @Test
    public void testHashCode() throws IOException {
        String testString = "test message";
        byte[] data = generateByteArray(EventProtocol.STRING_MESSAGE_EVENT, testString);
        StringMessageEvent eventFromBytes = new StringMessageEvent(data);
        StringMessageEvent eventFromString = new StringMessageEvent(testString);
        assertEquals(eventFromBytes.hashCode(), eventFromString.hashCode());

        Set<Event> events = new HashSet<>();
        events.add(eventFromBytes);
        events.add(eventFromString);
        assertEquals(1, events.size());
    }
}
