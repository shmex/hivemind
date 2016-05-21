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
        dataInputStream.read(messageData, 0, messageLength);
        message = new String(messageData);
    }
}
