package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NorGateTest {

    @Test
    void norShouldReturnTrueWhenBothInputsAreFalse() {
        Input a = new Input("A", false);
        Input b = new Input("B", false);

        NorGate gate = new NorGate("NOR1", a, b);

        assertTrue(gate.evaluate());
    }

    @Test
    void norShouldReturnFalseWhenOneInputIsTrue() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        NorGate gate = new NorGate("NOR1", a, b);

        assertFalse(gate.evaluate());
    }
}