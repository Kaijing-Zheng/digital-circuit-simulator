package com.circuitsim;

import java.util.Map;

public class DFlipFlop extends Gate {

    private boolean q = false;
    private boolean previousClock = false;

    public DFlipFlop(String name) {
        super(name, 2, 15);
    }

    @Override
    public boolean evaluate() {
        boolean d = getInput(0).evaluate();
        boolean clock = getInput(1).evaluate();

        if (!previousClock && clock) {
            q = d;
        }

        previousClock = clock;

        return q;
    }

    @Override
    public boolean evaluate(
        Map<Component, Boolean> values
    ) {
        boolean d = values.get(getInput(0));
        boolean clock = values.get(getInput(1));

        if (!previousClock && clock) {
            q = d;
        }

        previousClock = clock;

        return q;
    }
}