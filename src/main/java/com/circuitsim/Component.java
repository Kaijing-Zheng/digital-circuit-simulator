package com.circuitsim;

import java.util.Map;

public abstract class Component {

    private String name;

    public Component(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean evaluate();

    public boolean evaluate(Map<Component, Boolean> values) {
        return evaluate();
    }
}