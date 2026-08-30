package com.circuitsim;

import java.util.ArrayList;
import java.util.List;

public abstract class Gate extends Component {

    private List<Component> inputs;

    public Gate(String name, int inputCount) {
        super(name);

        inputs = new ArrayList<>();

        for (int i = 0; i < inputCount; i++) {
            inputs.add(null);
        }
    }

    public void connectInput(int index, Component component) {
        if (index < 0 || index >= inputs.size()) {
            throw new IllegalArgumentException("Invalid input index");
        }

        inputs.set(index, component);
    }

    protected Component getInput(int index) {
        Component input = inputs.get(index);

        if (input == null) {
            throw new IllegalStateException(
                getName() + " input " + index + " is not connected"
            );
        }

        return input;
    }

    public int getInputCount() {
        return inputs.size();
    }
}