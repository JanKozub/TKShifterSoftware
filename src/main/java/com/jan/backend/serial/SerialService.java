package com.jan.backend.serial;

import com.jan.backend.TimeWatch;
import com.jan.frontend.components.alerts.ClosePortErrorAlert;
import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.util.concurrent.CopyOnWriteArrayList;

public class SerialService {

    private final SerialPort serialPort;
    private Thread thread;
    private volatile int mode = -1;
    private final CopyOnWriteArrayList<SerialServiceListener> listeners = new CopyOnWriteArrayList<>();
    private final Object lock = new Object();
    private final SerialData serialData = new SerialData(null);

    public SerialService(String port) {
        serialPort = new SerialPort(port);
    }

    public void addListener(SerialServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SerialServiceListener listener) {
        listeners.remove(listener);
    }

    public boolean isPortValid() throws SerialPortException {
        if (!serialPort.isOpened()) openPort();
        if (thread == null) runNewThread();

        mode = -1;
        writeString("getMode");
        TimeWatch timeWatch = new TimeWatch(3000);
        do {
            synchronized (lock) {
                try {
                    if (mode != -1) return true;
                    lock.wait(timeWatch.getTimeLeft());
                } catch (InterruptedException e) {
                    serialPort.closePort();
                    return false;
                }
            }

            if (timeWatch.isTimedOut()) {
                serialPort.closePort();
                return false;
            }
        } while (true);
    }

    private void runNewThread() {
        thread = new Thread(this::getDataFromShifter);
        thread.start();
    }

    private void openPort() throws SerialPortException {
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
    }

    public synchronized void writeString(String str) throws SerialPortException {
        serialPort.writeString(str);
    }

    @SuppressWarnings("BusyWait")
    private String readString() throws SerialPortTimeoutException, SerialPortException, InterruptedException {
        StringBuilder read = new StringBuilder();
        while (!read.toString().contains("\n")) {
            String str = serialPort.readString(1, 6000);
            if (str.isEmpty())
                Thread.sleep(10);
            else {
                read.append(str);
            }
        }
        return read.toString().trim();
    }

    private void getDataFromShifter() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String read = readString();
                if (read.contains("values")) {
                    reportValue(read.split("=")[1].split(";"));
                } else if (read.contains("mode")) {
                    synchronized (lock) {
                        if (read.contains("1"))
                            mode = 1;
                        else if (read.contains("2"))
                            mode = 2;
                        else mode = -1;
                        lock.notifyAll();
                    }
                }

            }
        } catch (SerialPortException | SerialPortTimeoutException serialPortException) {
            reportError(serialPortException);
        } catch (InterruptedException ignored) {

        }
    }

    public void onClose(SerialServiceListener serialServiceListener) {
        try {
            removeListener(serialServiceListener);
            thread.interrupt();
            thread.join(100);
            serialPort.closePort();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (SerialPortException ex2) {
            Platform.runLater(() -> new ClosePortErrorAlert().showAndWait());
        }
    }

    public SerialData getSerialData() {
        return serialData;
    }

    private void reportError(Exception exception) {
        SerialPortErrorEvent errorEvent = new SerialPortErrorEvent(exception);
        listeners.forEach(listener -> listener.onSerialPortError(errorEvent));
    }

    private void reportValue(String[] data) {
        serialData.updateData(data);
        listeners.forEach(listener -> listener.onValueUpdate(serialData));
    }
}
