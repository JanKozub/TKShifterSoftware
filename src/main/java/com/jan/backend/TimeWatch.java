package com.jan.backend;

public class TimeWatch {
    private long starts;
    private final long timeout;

    public TimeWatch(long timeout) {
        reset();
        this.timeout = timeout;
    }

    public void reset() {
        starts = System.currentTimeMillis();
    }

    public long time() {
        long ends = System.currentTimeMillis();
        return ends - starts;
    }

    public boolean isTimedOut(){
        return time() > timeout;
    }

    public long getTimeLeft() {
        return Math.max(timeout - time(), 1);
    }
}
