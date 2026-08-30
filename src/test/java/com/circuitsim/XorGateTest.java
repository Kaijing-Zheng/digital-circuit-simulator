package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XorGateTest {

    @Test
    void xorGateShouldReturnTrueWhenInputsAreDifferent() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        XorGate gate = new XorGate("XOR1", a, b);

        assertTrue(gate.evaluate());
    }

    @Test
    void xorGateShouldReturnFalseWhenInputsAreTheSame() {
        Input a = new Input("A", true);
        Input b = new Input("B", true);

        XorGate gate = new XorGate("XOR1", a, b);

        assertFalse(gate.evaluate());
    }
}