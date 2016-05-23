package com.sprice.hivemind;

import com.sprice.hivemind.dht.node.registry.Registry;
import com.sprice.hivemind.dht.node.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        LOG.info("starting registry...");
        Registry registry = new Registry();
        registry.startServer(10865);

        Worker worker = new Worker();
        worker.connect(InetAddress.getLocalHost(), 10865);

        Scanner keyboard = new Scanner(System.in);
        String command = keyboard.nextLine();
        while(!command.equalsIgnoreCase("quit")) {
            switch(command) {
            }
            command = keyboard.nextLine();
        }
        worker.shutdown();
        registry.shutdown();
    }
}
