package com.circuitsim;

public class HalfAdder {

    private Input a;
    private Input b;

    private XorGate sumGate;
    private AndGate carryGate;

    public HalfAdder(boolean aValue, boolean bValue) {
        a = new Input("A", aValue);
        b = new Input("B", bValue);

        sumGate = new XorGate("SUM", a, b);
        carryGate = new AndGate("CARRY", a, b);
    }

    public boolean getSum() {
        return sumGate.evaluate();
    }

    public boolean getCarry() {
        return carryGate.evaluate();
    }

    public void setA(boolean value) {
        a.setValue(value);
    }

    public void setB(boolean value) {
        b.setValue(value);
    }
}