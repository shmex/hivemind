package com.sprice.hivemind;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventHandler;
import com.sprice.hivemind.dht.node.registry.Registry;
import com.sprice.hivemind.dht.node.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Application implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        LOG.info("starting registry...");
        Registry registry = new Registry();
        registry.startServer(10865);

        Scanner keyboard = new Scanner(System.in);
        String command = keyboard.nextLine();
        while(!command.equalsIgnoreCase("quit")) {
            String[] cmds = command.split(" ");
            switch(cmds[0]) {
                case "connect":
                    int port = 0;
                    if(cmds.length < 3) {
                        try {
                            port = Integer.parseInt(cmds[2]);
                        } catch(NumberFormatException e) {
                        }
                        if(port > 0)
                        LOG.error("Usage: connect <address> <port>");
                    }
                    try {
                        registry.connect(InetAddress.getByName(cmds[1]), Integer.parseInt(cmds[2]));
                    } catch(Exception e) {
                        LOG.error(String.format("Failed to connect to: %s:%s %s", cmds[1], cmds[2], e.getMessage()));
                    }
            }
            command = keyboard.nextLine();
        }
        registry.shutdown();
    }

    @Override
    public void onEvent(Event event) {
        LOG.info("RECEIVED EVENT: " + event.getType());
    }
}
