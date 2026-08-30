package com.circuitsim;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimingSimulatorTest {

    @Test
    void simulatesPropagationDelay() {

        Input input1 =
            new Input("INPUT1", true);

        Input input2 =
            new Input("INPUT2", true);

        AndGate andGate =
            new AndGate("AND1");

        NotGate notGate =
            new NotGate("NOT1");
        
        assertEquals(
            10,
            andGate.getPropagationDelayNs()
        );

        assertEquals(
            5,
            notGate.getPropagationDelayNs()
        );

        Circuit circuit =
            new Circuit("Timing Test");

        circuit.addComponent(input1);
        circuit.addComponent(input2);
        circuit.addComponent(andGate);
        circuit.addComponent(notGate);

        circuit.connect(
            input1,
            andGate,
            0
        );

        circuit.connect(
            input2,
            andGate,
            1
        );

        circuit.connect(
            andGate,
            notGate,
            0
        );

        TimingSimulator simulator =
            new TimingSimulator();

        Map<Component, List<WaveformPoint>> waveforms =
            simulator.simulate(circuit);

        List<WaveformPoint> andWaveform =
            waveforms.get(andGate);

        List<WaveformPoint> notWaveform =
            waveforms.get(notGate);

        assertEquals(
            10,
            andWaveform.get(0).getTimeNs()
        );

        assertEquals(
            true,
            andWaveform.get(0).getValue()
        );

        assertEquals(
            15,
            notWaveform.get(0).getTimeNs()
        );

        assertEquals(
            false,
            notWaveform.get(0).getValue()
        );
    }
}