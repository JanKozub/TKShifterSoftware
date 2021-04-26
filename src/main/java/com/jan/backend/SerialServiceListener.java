package com.jan.backend;

public interface SerialServiceListener {

    default void onSerialPortError(SerialPortErrorEvent event) {}

    default void onValueUpdate(SerialPortValueEvent event) {}
}
