package com.sprice.hivemind.dht.event;

import java.io.*;

/**
 * Event for sending a String message.
 * Wireformat: Event ->
 *     int MessageLength
 *     byte[MessageLength] Message
 */
public class StringMessageEvent extends Event {

    private String message;

    public StringMessageEvent(String message) {
        this.message = message;
    }

    public StringMessageEvent(byte[] data) throws IOException {
        super(data);
    }

    @Override
    public int getType() {
        return EventProtocol.STRING_MESSAGE_EVENT;
    }

    @Override
    protected void serialize(final DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
    }

    @Override
    protected void deserialize(final DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        int messageLength = dataInputStream.readInt();
        byte[] messageData = new byte[messageLength];
        dataInputStream.readFully(messageData, 0, messageLength);
        message = new String(messageData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringMessageEvent that = (StringMessageEvent) o;

        return message.equals(that.message);

    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}