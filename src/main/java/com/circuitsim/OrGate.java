package com.circuitsim;

public class OrGate extends Gate {

    public OrGate(String name) {
        super(name, 2);
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
}