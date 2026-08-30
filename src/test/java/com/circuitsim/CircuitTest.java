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

    @Test
    void circuitShouldConnectComponents() {
        Circuit circuit = new Circuit("Test Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", true);
        AndGate gate = new AndGate("AND1");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(gate);

        circuit.connect(a, gate, 0);
        circuit.connect(b, gate, 1);

        assertEquals(2, circuit.getWireCount());
        assertTrue(gate.evaluate());
    }

    @Test
    void connectedCircuitShouldUpdateWhenInputChanges() {
        Circuit circuit = new Circuit("Test Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", false);
        AndGate gate = new AndGate("AND1");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(gate);

        circuit.connect(a, gate, 0);
        circuit.connect(b, gate, 1);

        assertFalse(gate.evaluate());

        b.setValue(true);

        assertTrue(gate.evaluate());
    }

    @Test
    void circuitShouldEvaluateMultiLevelConnections() {
        Circuit circuit = new Circuit("Multi Level Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", true);
        Input c = new Input("C", false);

        AndGate andGate = new AndGate("AND1");
        XorGate xorGate = new XorGate("XOR1");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(c);
        circuit.addComponent(andGate);
        circuit.addComponent(xorGate);

        circuit.connect(a, andGate, 0);
        circuit.connect(b, andGate, 1);

        circuit.connect(andGate, xorGate, 0);
        circuit.connect(c, xorGate, 1);

        assertTrue(xorGate.evaluate());
    }
}