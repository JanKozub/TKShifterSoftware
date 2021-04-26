package com.jan.backend.serial;

import com.jan.backend.TimeWatch;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("BusyWait")
public class SerialService {

    private final SerialPort serialPort;
    private Thread thread;
    private volatile String[] data = new String[11];
    private volatile int mode = -1;
    private final CopyOnWriteArrayList<SerialServiceListener> listeners = new CopyOnWriteArrayList<>();

    public SerialService(String port) throws SerialPortException {
        serialPort = new SerialPort(port);
    }

    public void addListener(SerialServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SerialServiceListener listener) {
        listeners.remove(listener);
    }

    public String[] getData() {
        return data;
    }

    public int isPortValid() throws SerialPortException, SerialPortTimeoutException {
        if (!serialPort.isOpened()) {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
        }
        if (thread == null) {
            thread = new Thread(this::getDataFromShifter);
            thread.start();
        }

        mode = -1;
        writeString("getMode");
        TimeWatch timeWatch = new TimeWatch();
        do {
            if (mode != -1) return mode;
            if (timeWatch.time() > 6000) return -1;

            try {
                Thread.sleep(10); //TODO
            } catch (InterruptedException e) {
                return -1;
            }
        } while (true);
    }

    public synchronized void writeString(String str) throws SerialPortException {
        serialPort.writeString(str);
    }

    private String readString() throws SerialPortTimeoutException, SerialPortException {
        StringBuilder read = new StringBuilder();
        while (!read.toString().contains("\n")) {
            read.append(serialPort.readString(1, 6000));
        }

        return read.toString().trim();
    }

    private void getDataFromShifter() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String read = readString();
                if (read.contains("values")) {
                    data = read.split("=")[1].split(";");
//                    if (data != null)
                    reportValue(data);
                } else if (read.contains("mode")) {
                    if (read.contains("1"))
                        mode = 1;
                    else if (read.contains("2"))
                        mode = 2;
                    else mode = -1;
                }
                Thread.sleep(10);
            }
        } catch (SerialPortException | SerialPortTimeoutException serialPortException) {
            reportError(serialPortException);
        } catch (InterruptedException ignored) {

        }
    }

    public void onClose() throws SerialPortException {
        try {
            thread.interrupt();
            thread.join(100);
            serialPort.closePort();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void reportError(Exception exception) {
        SerialPortErrorEvent errorEvent = new SerialPortErrorEvent(exception);
        listeners.forEach(listener -> listener.onSerialPortError(errorEvent));
    }

    private void reportValue(String[] data) {
        SerialPortValueEvent serialPortValueEvent = new SerialPortValueEvent(data);
//        System.out.println(listeners);
        listeners.forEach(listener -> listener.onValueUpdate(serialPortValueEvent));
    }
}
