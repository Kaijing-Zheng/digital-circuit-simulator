package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WireTest {

    @Test
    void wireShouldStoreSourceAndDestination() {
        Input a = new Input("A", true);
        Input b = new Input("B", false);

        AndGate gate = new AndGate("AND1", a, b);

        Wire wire = new Wire(a, gate);

        assertEquals(a, wire.getSource());
        assertEquals(gate, wire.getDestination());
    }
}