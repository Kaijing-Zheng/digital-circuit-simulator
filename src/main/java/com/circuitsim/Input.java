package com.circuitsim;

public class Input extends Component {

    private boolean value;

    public Input(String name, boolean value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean evaluate() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}