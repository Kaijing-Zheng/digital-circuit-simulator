package com.circuitsim;

public class Wire {

    private Component source;
    private Component destination;

    public Wire(Component source, Component destination) {
        this.source = source;
        this.destination = destination;
    }

    public Component getSource() {
        return source;
    }

    public Component getDestination() {
        return destination;
    }
}