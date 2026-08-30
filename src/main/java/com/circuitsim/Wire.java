package com.circuitsim;

public class Wire {

    private Component source;
    private Gate destination;
    private int destinationInputIndex;

    public Wire(
        Component source,
        Gate destination,
        int destinationInputIndex
    ) {
        this.source = source;
        this.destination = destination;
        this.destinationInputIndex = destinationInputIndex;
    }

    public Component getSource() {
        return source;
    }

    public Gate getDestination() {
        return destination;
    }

    public int getDestinationInputIndex() {
        return destinationInputIndex;
    }
}