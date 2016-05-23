package com.sprice.hivemind.dht.event;

import java.io.*;

public class EventUtils {

    private static final EventUtils instance = new EventUtils();

    private EventUtils() {}

    public static EventUtils getInstance() {
        return instance;
    }

    public void writeBytes(byte[] data, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data, 0, data.length);
    }

    public byte[] readBytes(DataInputStream dataInputStream) throws IOException {
        int dataLen = dataInputStream.readInt();
        byte[] data = new byte[dataLen];
        dataInputStream.readFully(data, 0, dataLen);
        return data;
    }

    public void writeString(String str, DataOutputStream dataOutputStream) throws IOException {
        byte[] strData = str.getBytes(EventProtocol.CHARSET);
        writeBytes(strData, dataOutputStream);
    }

    public String readString(DataInputStream dataInputStream) throws IOException {
        byte[] strData = readBytes(dataInputStream);
        return new String(strData, EventProtocol.CHARSET);
    }
}
