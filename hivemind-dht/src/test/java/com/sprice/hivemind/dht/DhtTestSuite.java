package com.sprice.hivemind.dht;

import com.sprice.hivemind.dht.event.EventTestSuite;
import com.sprice.hivemind.dht.transport.TransportTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventTestSuite.class,
        TransportTestSuite.class
})
public class DhtTestSuite {
}
