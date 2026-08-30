package com.circuitsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitGraph {

    private Map<Component, List<Component>> adjacencyList;

    public CircuitGraph(Circuit circuit) {
        adjacencyList = new HashMap<>();

        for (Component component : circuit.getComponents()) {
            adjacencyList.put(component, new ArrayList<>());
        }

        for (Wire wire : circuit.getWires()) {
            Component source = wire.getSource();
            Component destination = wire.getDestination();

            adjacencyList.get(source).add(destination);
        }
    }

    public List<Component> getNeighbors(Component component) {
        return adjacencyList.get(component);
    }

    public Map<Component, List<Component>> getAdjacencyList() {
        return adjacencyList;
    }
}