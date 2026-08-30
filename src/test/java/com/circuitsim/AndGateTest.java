package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AndGateTest {

    @Test
    void andGateShouldReturnTrueWhenBothInputsAreTrue() {
        Input a = new Input("A", true);
        Input b = new Input("B", true);

        AndGate gate = new AndGate("AND1", a, b);

        assertTrue(gate.evaluate());
    }

    @Test
    void andGateShouldReturnFalseWhenOneInputIsFalse() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        AndGate gate = new AndGate("AND1", a, b);

        assertFalse(gate.evaluate());
    }

    @Test
    void andGateShouldUpdateWhenInputChanges() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        AndGate gate = new AndGate("AND1", a, b);

        assertFalse(gate.evaluate());

        b.setValue(true);

        assertTrue(gate.evaluate());
    }
}