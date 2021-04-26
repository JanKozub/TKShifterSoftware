package com.jan.backend.serial;

public interface SerialServiceListener {

    default void onSerialPortError(SerialPortErrorEvent event) {}

    default void onValueUpdate(SerialPortValueEvent event) {}
}
