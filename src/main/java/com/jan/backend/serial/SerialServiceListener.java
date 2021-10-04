package com.jan.backend.serial;

import java.util.EventListener;

public interface SerialServiceListener extends EventListener {

    default void onSerialPortError(SerialPortErrorEvent event) {}

    default void onValueUpdate(SerialData data) {}
}
