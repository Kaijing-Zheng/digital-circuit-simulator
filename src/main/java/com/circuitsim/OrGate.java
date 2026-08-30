package com.circuitsim;

public class OrGate extends Component {

    private Component input1;
    private Component input2;

    public OrGate(String name, Component input1, Component input2) {
        super(name);
        this.input1 = input1;
        this.input2 = input2;
    }

    @Override
    public boolean evaluate() {
        return input1.evaluate() || input2.evaluate();
    }
}