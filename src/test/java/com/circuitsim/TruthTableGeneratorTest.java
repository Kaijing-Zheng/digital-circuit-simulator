package com.circuitsim;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TruthTableGeneratorTest {

    @Test
    void shouldGenerateTruthTableForAndGate() {
        Circuit circuit = new Circuit("AND Circuit");

        Input a = new Input("A", false);
        Input b = new Input("B", false);

        AndGate andGate = new AndGate("OUT");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(andGate);

        circuit.connect(a, andGate, 0);
        circuit.connect(b, andGate, 1);

        TruthTableGenerator generator =
            new TruthTableGenerator();

        List<Map<String, Boolean>> table =
            generator.generate(
                circuit,
                List.of(a, b),
                List.of(andGate)
            );

        assertEquals(4, table.size());

        assertFalse(table.get(0).get("A"));
        assertFalse(table.get(0).get("B"));
        assertFalse(table.get(0).get("OUT"));

        assertTrue(table.get(3).get("A"));
        assertTrue(table.get(3).get("B"));
        assertTrue(table.get(3).get("OUT"));
    }

    @Test
    void shouldGenerateTruthTableForHalfAdderCircuit() {
        Circuit circuit = new Circuit("Half Adder");

        Input a = new Input("A", false);
        Input b = new Input("B", false);

        XorGate sum = new XorGate("SUM");
        AndGate carry = new AndGate("CARRY");

        circuit.addComponent(a);
        circuit.addComponent(b);
        circuit.addComponent(sum);
        circuit.addComponent(carry);

        circuit.connect(a, sum, 0);
        circuit.connect(b, sum, 1);

        circuit.connect(a, carry, 0);
        circuit.connect(b, carry, 1);

        TruthTableGenerator generator =
            new TruthTableGenerator();

        List<Map<String, Boolean>> table =
            generator.generate(
                circuit,
                List.of(a, b),
                List.of(sum, carry)
            );

        assertEquals(4, table.size());

        assertFalse(table.get(0).get("SUM"));
        assertFalse(table.get(0).get("CARRY"));

        assertTrue(table.get(1).get("SUM"));
        assertFalse(table.get(1).get("CARRY"));

        assertTrue(table.get(2).get("SUM"));
        assertFalse(table.get(2).get("CARRY"));

        assertFalse(table.get(3).get("SUM"));
        assertTrue(table.get(3).get("CARRY"));
    }
}