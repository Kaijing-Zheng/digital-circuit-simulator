package com.circuitsim;

import java.util.Map;

public class XnorGate extends Gate {

    private static final long DEFAULT_DELAY_NS = 12;

    public XnorGate(String name) {
        super(name, 2, DEFAULT_DELAY_NS);
    }

    @Override
    public boolean evaluate() {
        return getInput(0).evaluate()
            == getInput(1).evaluate();
    }

    @Override
    public boolean evaluate(
        Map<Component, Boolean> values
    ) {
        return values.get(getInput(0))
            == values.get(getInput(1));
    }
}