package com.sprice.hivemind.dht.event;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

    public String getMessage() {
        return message;
    }

    @Override
    public int getType() {
        return EventProtocol.STRING_MESSAGE_EVENT;
    }

    @Override
    protected void serialize(final DataOutputStream dataOutputStream) throws IOException {
        super.serialize(dataOutputStream);
        byte[] messageData = message.getBytes(EventProtocol.CHARSET);
        dataOutputStream.writeInt(messageData.length);
        dataOutputStream.write(messageData, 0, messageData.length);
    }

    @Override
    protected void deserialize(final DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        int messageLength = dataInputStream.readInt();
        byte[] messageData = new byte[messageLength];
        dataInputStream.readFully(messageData, 0, messageLength);
        message = new String(messageData, EventProtocol.CHARSET);
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
