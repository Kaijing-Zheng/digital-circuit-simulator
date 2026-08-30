package com.circuitsim;

public class TimingEvent
    implements Comparable<TimingEvent> {

    private final long timeNs;
    private final Component component;
    private final boolean value;

    public TimingEvent(
        long timeNs,
        Component component,
        boolean value
    ) {
        this.timeNs = timeNs;
        this.component = component;
        this.value = value;
    }

    public long getTimeNs() {
        return timeNs;
    }

    public Component getComponent() {
        return component;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public int compareTo(TimingEvent other) {
        return Long.compare(
            this.timeNs,
            other.timeNs
        );
    }
}