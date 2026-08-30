package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrGateTest {

    @Test
    void orGateShouldReturnFalseWhenBothInputsAreFalse() {
        Input a = new Input("A", false);
        Input b = new Input("B", false);

        OrGate gate = new OrGate("OR1", a, b);

        assertFalse(gate.evaluate());
    }

    @Test
    void orGateShouldReturnTrueWhenOneInputIsTrue() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        OrGate gate = new OrGate("OR1", a, b);

        assertTrue(gate.evaluate());
    }
}