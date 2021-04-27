package com.jan.backend.serial;


public class SerialPortValueEvent { //zalety, wady, warianty, inne rozwiazania

    private final String[] data;

    public SerialPortValueEvent(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }
}


