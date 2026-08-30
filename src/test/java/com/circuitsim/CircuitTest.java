package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircuitTest {

    @Test
    void circuitShouldStartEmpty() {
        Circuit circuit = new Circuit("Test Circuit");

        assertEquals(0, circuit.getComponentCount());
    }

    @Test
    void circuitShouldStoreComponents() {
        Circuit circuit = new Circuit("Test Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", false);
        AndGate andGate = new AndGate("AND1", a, b);

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(andGate);

        assertEquals(3, circuit.getComponentCount());
    }

    @Test
    void circuitShouldReturnItsName() {
        Circuit circuit = new Circuit("Half Adder");

        assertEquals("Half Adder", circuit.getName());
    }
}