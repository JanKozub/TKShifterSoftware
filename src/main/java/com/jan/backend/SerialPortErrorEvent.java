package com.jan.backend;

public class SerialPortErrorEvent {
    private final Exception error;

    public SerialPortErrorEvent(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }
}
