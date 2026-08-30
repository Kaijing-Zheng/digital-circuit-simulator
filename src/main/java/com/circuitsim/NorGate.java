package com.circuitsim;

import java.util.Map;

public class NorGate extends Gate {

    public NorGate(String name) {
        super(name, 2);
    }

    public NorGate(String name, Component input1, Component input2) {
        this(name);

        connectInput(0, input1);
        connectInput(1, input2);
    }

    @Override
    public boolean evaluate() {
        return !(getInput(0).evaluate() || getInput(1).evaluate());
    }

    @Override
    public boolean evaluate(Map<Component, Boolean> values) {
        return !(values.get(getInput(0)) || values.get(getInput(1)));
    }
}