package com.circuitsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.List;

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

    public Map<Component, Integer> calculateInDegrees() {
        Map<Component, Integer> inDegrees = new HashMap<>();

        for (Component component : adjacencyList.keySet()) {
            inDegrees.put(component, 0);
        }

        for (Component source : adjacencyList.keySet()) {
            for (Component destination : adjacencyList.get(source)) {
                int currentInDegree = inDegrees.get(destination);
                inDegrees.put(destination, currentInDegree + 1);
            }
        }

        return inDegrees;
    }

    public List<Component> topologicalSort() {
        Map<Component, Integer> inDegrees = calculateInDegrees();

        Queue<Component> ready = new LinkedList<>();

        for (Component component : inDegrees.keySet()) {
            if (inDegrees.get(component) == 0) {
                ready.add(component);
            }
        }

        List<Component> result = new ArrayList<>();

        while (!ready.isEmpty()) {
            Component current = ready.remove();

            result.add(current);

            for (Component neighbor : adjacencyList.get(current)) {
                int newInDegree = inDegrees.get(neighbor) - 1;
                inDegrees.put(neighbor, newInDegree);

                if (newInDegree == 0) {
                    ready.add(neighbor);
                }
            }
        }

        return result;
    }
}