package com.circuitsim;

public class NotGate extends Gate {

    public NotGate(String name) {
        super(name, 1);
    }

    public NotGate(String name, Component input) {
        this(name);

        connectInput(0, input);
    }

    @Override
    public boolean evaluate() {
        return !getInput(0).evaluate();
    }
}