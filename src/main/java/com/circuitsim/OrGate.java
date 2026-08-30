package com.circuitsim;

import java.util.Map;

public class OrGate extends Gate {

    public OrGate(String name) {
        super(name, 2, 10);
    }

    public OrGate(String name, Component input1, Component input2) {
        this(name);

        connectInput(0, input1);
        connectInput(1, input2);
    }

    @Override
    public boolean evaluate() {
        return getInput(0).evaluate() || getInput(1).evaluate();
    }

    @Override
    public boolean evaluate(Map<Component, Boolean> values) {
        return values.get(getInput(0)) || values.get(getInput(1));
    }
}