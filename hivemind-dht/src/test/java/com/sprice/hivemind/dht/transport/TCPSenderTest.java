package com.sprice.hivemind.dht.transport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class TCPSenderTest {

    private TCPSender tcpSender;
    private Socket socket;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void initMocks() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        mockSocket();
        tcpSender = new TCPSender(null, socket);
    }

    private void mockSocket() throws IOException {
        socket = Mockito.mock(Socket.class);
        Mockito.when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
    }

    @Test
    public void testSend() throws IOException {
        byte[] testData = {0, 2, 1, 4, 5};
        tcpSender.send(testData);
        byte[] sentData = byteArrayOutputStream.toByteArray();
        byte[] expectedData = {0, 0, 0, 5, 0, 2, 1, 4, 5}; //length prepended int
        assertTrue(Arrays.equals(expectedData, sentData));
    }
}
