package com.circuitsim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitSimulator {

    public Map<Component, Boolean> simulate(Circuit circuit) {
        CircuitGraph graph = new CircuitGraph(circuit);

        List<Component> order = graph.topologicalSort();

        Map<Component, Boolean> results = new HashMap<>();

        for (Component component : order) {
            boolean value = component.evaluate();
            results.put(component, value);
        }

        return results;
    }
}