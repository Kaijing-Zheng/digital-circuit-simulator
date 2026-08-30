package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NandGateTest {

    @Test
    void nandShouldReturnFalseWhenBothInputsAreTrue() {
        Input a = new Input("A", true);
        Input b = new Input("B", true);

        NandGate gate = new NandGate("NAND1", a, b);

        assertFalse(gate.evaluate());
    }

    @Test
    void nandShouldReturnTrueWhenOneInputIsFalse() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        NandGate gate = new NandGate("NAND1", a, b);

        assertTrue(gate.evaluate());
    }
}