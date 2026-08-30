package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {

    @Test
    void inputShouldReturnInitialValue() {
        Input input = new Input("A", true);

        assertTrue(input.evaluate());
    }

    @Test
    void inputValueShouldBeChangeable() {
        Input input = new Input("A", false);

        input.setValue(true);

        assertTrue(input.evaluate());
    }
}