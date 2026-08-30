package com.circuitsim;

import java.util.ArrayList;
import java.util.List;

public class Circuit {

    private String name;
    private List<Component> components;
    private List<Wire> wires;

    public Circuit(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.wires = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public List<Component> getComponents() {
        return components;
    }

    public int getComponentCount() {
        return components.size();
    }

    public void addWire(Wire wire) {
        wires.add(wire);
    }

    public List<Wire> getWires() {
        return wires;
    }

    public int getWireCount() {
        return wires.size();
    }
}