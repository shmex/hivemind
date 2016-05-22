package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.transport.TCPSender;
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

    @InjectMocks private TCPSender tcpSender;
    @Mock private Socket socket;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void initMocks() throws IOException {
        MockitoAnnotations.initMocks(this);
        byteArrayOutputStream = new ByteArrayOutputStream();
        mockSocket();
    }

    private void mockSocket() throws IOException {
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
