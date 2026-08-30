package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WireTest {

    @Test
    void wireShouldStoreConnectionInformation() {
        Input a = new Input("A", true);
        AndGate gate = new AndGate("AND1");

        Wire wire = new Wire(a, gate, 0);

        assertEquals(a, wire.getSource());
        assertEquals(gate, wire.getDestination());
        assertEquals(0, wire.getDestinationInputIndex());
    }
}