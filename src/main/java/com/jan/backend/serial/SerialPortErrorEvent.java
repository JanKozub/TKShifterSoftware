package com.jan.backend.serial;

public class SerialPortErrorEvent {
    private final Exception error;

    public SerialPortErrorEvent(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }
}
