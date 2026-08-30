package com.circuitsim;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CircuitSimulatorTest {

    @Test
    void simulatorShouldEvaluateSimpleCircuit() {
        Circuit circuit = new Circuit("Simple Circuit");

        Input a = new Input("A", true);
        Input b = new Input("B", true);

        AndGate andGate = new AndGate("AND1");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(andGate);

        circuit.connect(a, andGate, 0);
        circuit.connect(b, andGate, 1);

        CircuitSimulator simulator = new CircuitSimulator();

        Map<Component, Boolean> results = simulator.simulate(circuit);

        assertTrue(results.get(a));
        assertTrue(results.get(b));
        assertTrue(results.get(andGate));
    }

    @Test
    void simulatorShouldEvaluateMultiLevelCircuit() {
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

        CircuitSimulator simulator = new CircuitSimulator();

        Map<Component, Boolean> results = simulator.simulate(circuit);

        assertTrue(results.get(andGate));
        assertTrue(results.get(xorGate));
    }
}