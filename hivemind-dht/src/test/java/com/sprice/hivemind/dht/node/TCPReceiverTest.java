package com.sprice.hivemind.dht.node;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.StringMessageEvent;
import com.sprice.hivemind.dht.transport.TCPReceiver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.*;
import java.net.Socket;

import static junit.framework.TestCase.assertEquals;

public class TCPReceiverTest {

    @Mock private Socket socket;
    @Mock private Node node;
    private PipedOutputStream pipedOutputStream;

    @Before
    public void initMocks() throws IOException {
        MockitoAnnotations.initMocks(this);
        pipedOutputStream = new PipedOutputStream();
        mockSocket();
    }

    private void mockSocket() throws IOException {
        Mockito.when(socket.getInputStream()).thenReturn(new PipedInputStream(pipedOutputStream));
    }

    @Test
    public void testReceive() throws IOException, InterruptedException {
        TCPReceiver tcpReceiver = new TCPReceiver(node, socket);
        Thread thread = new Thread(tcpReceiver);
        thread.start();

        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);
        String testString = "test message";
        byte[] testData = testString.getBytes("UTF-8");
        dataOutputStream.writeInt(testData.length + 8); //write total length
        dataOutputStream.writeInt(1); //write event type
        dataOutputStream.writeInt(testData.length); //write message length
        dataOutputStream.write(testData, 0, testData.length);
        dataOutputStream.flush();

        Thread.sleep(50); // give some processing time before the interrupt
        thread.interrupt();
        thread.join();

        final ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        Mockito.verify(node).onEvent(captor.capture());
        StringMessageEvent expectedEvent = new StringMessageEvent(testString);
        assertEquals(expectedEvent, captor.getValue());
    }
}
