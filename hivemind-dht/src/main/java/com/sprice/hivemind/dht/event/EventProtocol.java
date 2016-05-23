package com.sprice.hivemind.dht.event;

public interface EventProtocol {

    String CHARSET = "UTF-8";

    /* Reserved Bogus Event */
    int BOGUS_EVENT = 0;

    /* Core Node Events */
    int SOCKET_EVENT = 1;
    int ERROR_EVENT = 2;
    int INCOMING_CONNECTION_EVENT = 3;
    int CONNECTION_ERROR_EVENT = 4;
    int SERVER_ERROR_EVENT = 5;
    int SERVER_STARTED_EVENT = 6;
    int SERVER_STOPPED_EVENT = 7;
    int OUTGOING_CONNECTION_EVENT = 8;
    int SEND_EVENT = 9;
    int SHUTDOWN_EVENT = 10;
    int SHUTDOWN_START_EVENT = 11;
    int SHUTDOWN_COMPLETE_EVENT = 12;
    int SUBMIT_TASK_EVENT = 13;
    int EVENT_HANDLER_REGISTRATION_EVENT = 14;
    int EVENT_HANDLER_UNREGISTRATION_EVENT =15;

    /* Custom Events */
    int STRING_MESSAGE_EVENT = 16;
}
