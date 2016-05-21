package com.sprice.hivemind.dht.event;

import java.io.*;

public abstract class Event {

    public Event() {
    }

    public Event(byte[] data) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            deserialize(dataInputStream);
        } finally {
            dataInputStream.close();
        }
    }

    public abstract int getType();

    public byte[] getData() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            serialize(dataOutputStream);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } finally {
            dataOutputStream.close();
        }
    }

    protected void deserialize(final DataInputStream dataInputStream) throws IOException {
        int type = dataInputStream.readInt();
        if(type != getType()) {
            throw new IllegalArgumentException("Can not construct Event: " + getClass().getName() +
                    ". Expected eventType: " + getType() + " but received: " + type);
        }
    }

    protected void serialize(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(getType());
    }
}
