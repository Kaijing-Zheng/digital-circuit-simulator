package com.circuitsim;

import org.junit.jupiter.api.Test;
import java.util.List;

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

    @Test
    void graphShouldCalculateInDegrees() {
        Circuit circuit = new Circuit("Test Circuit");

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

        CircuitGraph graph = new CircuitGraph(circuit);

        assertEquals(0, graph.calculateInDegrees().get(a));
        assertEquals(0, graph.calculateInDegrees().get(b));
        assertEquals(0, graph.calculateInDegrees().get(c));

        assertEquals(2, graph.calculateInDegrees().get(andGate));
        assertEquals(2, graph.calculateInDegrees().get(xorGate));
    }

    @Test
    void topologicalSortShouldPlaceDependenciesBeforeGates() {
        Circuit circuit = new Circuit("Test Circuit");

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

        CircuitGraph graph = new CircuitGraph(circuit);

        List<Component> order = graph.topologicalSort();

        assertTrue(order.indexOf(a) < order.indexOf(andGate));
        assertTrue(order.indexOf(b) < order.indexOf(andGate));

        assertTrue(order.indexOf(andGate) < order.indexOf(xorGate));
        assertTrue(order.indexOf(c) < order.indexOf(xorGate));
    }

    @Test
    void topologicalSortShouldDetectCycle() {
        Circuit circuit = new Circuit("Cyclic Circuit");

        Input a = new Input("A", true);
        AndGate andGate = new AndGate("AND1");
        NotGate notGate = new NotGate("NOT1");

        circuit.addComponent(a);
        circuit.addComponent(andGate);
        circuit.addComponent(notGate);

        circuit.connect(a, andGate, 0);
        circuit.connect(notGate, andGate, 1);
        circuit.connect(andGate, notGate, 0);

        assertThrows(
            IllegalStateException.class,
            () -> new CircuitGraph(circuit).topologicalSort()
        );
    }

    @Test
    void topologicalSortShouldProcessAllComponentsInValidCircuit() {
        Circuit circuit = new Circuit("Valid Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", false);

        AndGate andGate = new AndGate("AND1");
        NotGate notGate = new NotGate("NOT1");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(andGate);
        circuit.addComponent(notGate);

        circuit.connect(a, andGate, 0);
        circuit.connect(b, andGate, 1);
        circuit.connect(andGate, notGate, 0);

        CircuitGraph graph = new CircuitGraph(circuit);

        List<Component> order = graph.topologicalSort();

        assertEquals(4, order.size());
    }
}