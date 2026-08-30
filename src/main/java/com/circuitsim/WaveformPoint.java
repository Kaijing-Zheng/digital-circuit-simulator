package com.circuitsim;

public class WaveformPoint {

    private final long timeNs;
    private final boolean value;

    public WaveformPoint(long timeNs, boolean value) {
        this.timeNs = timeNs;
        this.value = value;
    }

    public long getTimeNs() {
        return timeNs;
    }

    public boolean getValue() {
        return value;
    }
}