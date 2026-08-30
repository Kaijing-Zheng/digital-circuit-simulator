package com.circuitsim;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class XnorGateTest {

    @Test
    void xnorIsTrueWhenBothInputsAreFalse() {
        Input a = new Input("A", false);
        Input b = new Input("B", false);

        XnorGate gate = new XnorGate("XNOR1");
        gate.connectInput(0, a);
        gate.connectInput(1, b);

        assertTrue(gate.evaluate());
    }

    @Test
    void xnorIsFalseWhenInputsAreDifferent() {
        Input a = new Input("A", false);
        Input b = new Input("B", true);

        XnorGate gate = new XnorGate("XNOR1");
        gate.connectInput(0, a);
        gate.connectInput(1, b);

        assertFalse(gate.evaluate());
    }

    @Test
    void xnorIsTrueWhenBothInputsAreTrue() {
        Input a = new Input("A", true);
        Input b = new Input("B", true);

        XnorGate gate = new XnorGate("XNOR1");
        gate.connectInput(0, a);
        gate.connectInput(1, b);

        assertTrue(gate.evaluate());
    }
}