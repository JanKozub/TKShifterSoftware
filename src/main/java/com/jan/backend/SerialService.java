package com.jan.backend;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

@SuppressWarnings("BusyWait")
public class SerialService {

    private final SerialPort serialPort;
    private Thread thread;
    private String[] data;

    public SerialService(String port) throws SerialPortException {
        serialPort = new SerialPort(port);
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public Thread getThread() {
        return thread;
    }

    public String[] getData() {
        return data;
    }

    public int isPortValid() throws SerialPortException, SerialPortTimeoutException {
        writeString("getMode");
        TimeWatch timeWatch = new TimeWatch();
        do {
            if (serialPort.isOpened()) {
                String msg = readString();
                if (msg.contains("mode")) {
                    thread = new Thread(this::getDataFromShifter);
                    thread.start();

                    if (msg.contains("1")) {
                        return 1;
                    } else {
                        return 2;
                    }
                }
            }
            if (timeWatch.time() > 3000) return -1;

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return -1;
            }
        } while (true);
    }

    public void writeString(String str) throws SerialPortException {
        serialPort.writeString(str);
    }

    public String readString() throws SerialPortTimeoutException, SerialPortException {
        StringBuilder read = new StringBuilder();
        while (!read.toString().contains("\n")) {
            read.append(serialPort.readString(1, 6000));
        }

        return read.toString().trim();
    }

    private void getDataFromShifter() {
        try {
            while (serialPort.isOpened() && !thread.isInterrupted()) {
                String read = readString();
                if (read.contains("values")) {
                    data = read.split("=")[1].split(";");
                }
                Thread.sleep(10);
            }
        } catch (SerialPortException | SerialPortTimeoutException | InterruptedException serialPortException) {
            thread.interrupt();
        }
    }
}
