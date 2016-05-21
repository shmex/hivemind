package com.sprice.hivemind.dht.event;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

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

}
