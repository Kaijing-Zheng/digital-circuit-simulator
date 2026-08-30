package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircuitGraphTest {

    @Test
    void graphShouldContainCircuitConnections() {
        Circuit circuit = new Circuit("Test Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", true);
        AndGate andGate = new AndGate("AND1");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(andGate);

        circuit.connect(a, andGate, 0);
        circuit.connect(b, andGate, 1);

        CircuitGraph graph = new CircuitGraph(circuit);

        assertTrue(graph.getNeighbors(a).contains(andGate));
        assertTrue(graph.getNeighbors(b).contains(andGate));
        assertEquals(0, graph.getNeighbors(andGate).size());
    }
}