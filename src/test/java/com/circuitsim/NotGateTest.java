package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotGateTest {

    @Test
    void notGateShouldInvertTrueToFalse() {
        Input input = new Input("A", true);

        NotGate gate = new NotGate("NOT1", input);

        assertFalse(gate.evaluate());
    }

    @Test
    void notGateShouldInvertFalseToTrue() {
        Input input = new Input("A", false);

        NotGate gate = new NotGate("NOT1", input);

        assertTrue(gate.evaluate());
    }
}