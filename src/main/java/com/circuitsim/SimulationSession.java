package com.circuitsim;

import java.util.Map;

public class SimulationSession {

    private final Circuit circuit;
    private final Map<String, Component> components;

    public SimulationSession(
        Circuit circuit,
        Map<String, Component> components
    ) {
        this.circuit = circuit;
        this.components = components;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public Map<String, Component> getComponents() {
        return components;
    }

    public Component getComponent(String name) {
        return components.get(name);
    }
}