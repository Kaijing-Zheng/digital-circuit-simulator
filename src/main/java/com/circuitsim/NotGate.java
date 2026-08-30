package com.circuitsim;

public class NotGate extends Component {

    private Component input;

    public NotGate(String name, Component input) {
        super(name);
        this.input = input;
    }

    @Override
    public boolean evaluate() {
        return !input.evaluate();
    }
}