package com.circuitsim;

import java.util.Map;

public abstract class Component {

    private String name;
    private long propagationDelayNs;

    public Component(String name) {
        this(name, 0);
    }

    public Component(String name, long propagationDelayNs) {
        this.name = name;
        this.propagationDelayNs = propagationDelayNs;
    }

    public String getName() {
        return name;
    }

    public long getPropagationDelayNs() {
        return propagationDelayNs;
    }

    public void setPropagationDelayNs(long propagationDelayNs) {
        this.propagationDelayNs = propagationDelayNs;
    }

    public abstract boolean evaluate();

    public boolean evaluate(Map<Component, Boolean> values) {
        return evaluate();
    }
}