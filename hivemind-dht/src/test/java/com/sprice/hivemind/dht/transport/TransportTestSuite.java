package com.sprice.hivemind.dht.transport;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TCPReceiverTest.class,
        TCPSenderTest.class
})
public class TransportTestSuite {
}
