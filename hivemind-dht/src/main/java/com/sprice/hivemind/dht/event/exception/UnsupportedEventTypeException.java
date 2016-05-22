package com.sprice.hivemind.dht.event.exception;

public class UnsupportedEventTypeException extends Exception {

    public UnsupportedEventTypeException() {
    }

    public UnsupportedEventTypeException(String message) {
        super(message);
    }

    public UnsupportedEventTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedEventTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedEventTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
