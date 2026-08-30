package com.circuitsim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HalfAdderTest {

    @Test
    void shouldProduceCorrectOutputForZeroZero() {
        HalfAdder halfAdder = new HalfAdder(false, false);

        assertFalse(halfAdder.getSum());
        assertFalse(halfAdder.getCarry());
    }

    @Test
    void shouldProduceCorrectOutputForZeroOne() {
        HalfAdder halfAdder = new HalfAdder(false, true);

        assertTrue(halfAdder.getSum());
        assertFalse(halfAdder.getCarry());
    }

    @Test
    void shouldProduceCorrectOutputForOneZero() {
        HalfAdder halfAdder = new HalfAdder(true, false);

        assertTrue(halfAdder.getSum());
        assertFalse(halfAdder.getCarry());
    }

    @Test
    void shouldProduceCorrectOutputForOneOne() {
        HalfAdder halfAdder = new HalfAdder(true, true);

        assertFalse(halfAdder.getSum());
        assertTrue(halfAdder.getCarry());
    }

    @Test
    void shouldUpdateWhenInputsChange() {
        HalfAdder halfAdder = new HalfAdder(false, false);

        halfAdder.setA(true);
        halfAdder.setB(true);

        assertFalse(halfAdder.getSum());
        assertTrue(halfAdder.getCarry());
    }
}