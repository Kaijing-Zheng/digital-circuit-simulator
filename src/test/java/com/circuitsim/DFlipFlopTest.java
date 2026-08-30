package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DFlipFlopTest {

    @Test
    void capturesDOnlyOnRisingClockEdge() {
        Input d = new Input("D", false);
        Input clock = new Input("CLK", false);

        DFlipFlop flipFlop =
            new DFlipFlop("DFF1");

        flipFlop.connectInput(0, d);
        flipFlop.connectInput(1, clock);

        // Initial state: D=0, CLK=0
        assertFalse(flipFlop.evaluate());

        // Change D to 1, but clock is still low.
        // Q should NOT change yet.
        d.setValue(true);

        assertFalse(flipFlop.evaluate());

        // Rising edge: CLK goes 0 -> 1.
        // Q should capture D=1.
        clock.setValue(true);

        assertTrue(flipFlop.evaluate());

        // Change D while clock remains high.
        // Q should stay 1.
        d.setValue(false);

        assertTrue(flipFlop.evaluate());

        // Falling edge: CLK goes 1 -> 0.
        // Q should still stay 1.
        clock.setValue(false);

        assertTrue(flipFlop.evaluate());

        // Next rising edge.
        // D is now 0, so Q captures 0.
        clock.setValue(true);

        assertFalse(flipFlop.evaluate());
    }
}