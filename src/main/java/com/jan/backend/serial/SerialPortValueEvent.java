package com.jan.backend.serial;


public class SerialPortValueEvent {

    private final String[] data;

    public SerialPortValueEvent(String[] data) {
        this.data = data;
    }

    public String getCurrentGear() {
        return data[1];
    }

    public int getMode() {
        return Integer.parseInt(data[0]);
    }

    public String getX() {
        return data[2];
    }

    public String getY() {
        return data[3];
    }

    public String getT() {
        return data[4];
    }

    public String getUCHP() {
        return data[9];
    }

    public String getLCHP() {
        return data[10];
    }

    public boolean isXInverted() {
        return data[11].equals("1");
    }

    public boolean isYInverted() {
        return data[12].equals("1");
    }

    public boolean isTInverted() {
        return data[13].equals("1");
    }

    public String getCurrentOffset(int addr) {
        return data[4 + addr];
    }

    public String getMessage() {
        return "mode= " + getMode() +
                ", CG=" + getCurrentGear() +
                ", X=" + getX() +
                ", Y=" + getY() +
                ", T=" + getT() +
                ", offset=[" + getCurrentOffset(1) +
                ", " + getCurrentOffset(2) +
                ", " + getCurrentOffset(3) +
                ", " + getCurrentOffset(4) + "]" +
                ", UCHP=" + getUCHP() +
                ", LCHP=" + getLCHP();
    }
}


