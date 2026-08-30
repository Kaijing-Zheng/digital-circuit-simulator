package com.circuitsim;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TimingSimulator {

    public Map<Component, List<WaveformPoint>> simulate(
        Circuit circuit
    ) {
        Map<Component, Boolean> values =
            new LinkedHashMap<>();

        Map<Component, List<WaveformPoint>> waveforms =
            new LinkedHashMap<>();

        PriorityQueue<TimingEvent> eventQueue =
            new PriorityQueue<>();

        for (Component component : circuit.getComponents()) {
            waveforms.put(
                component,
                new ArrayList<>()
            );

            if (component instanceof Input input) {
                eventQueue.add(
                    new TimingEvent(
                        0,
                        input,
                        input.evaluate()
                    )
                );
            }
        }

        while (!eventQueue.isEmpty()) {
            TimingEvent event =
                eventQueue.poll();

            Component component =
                event.getComponent();

            boolean oldValue =
                values.getOrDefault(
                    component,
                    false
                );

            boolean newValue =
                event.getValue();

            if (
                values.containsKey(component)
                && oldValue == newValue
            ) {
                continue;
            }

            values.put(
                component,
                newValue
            );

            waveforms
                .get(component)
                .add(
                    new WaveformPoint(
                        event.getTimeNs(),
                        newValue
                    )
                );

            for (Wire wire : circuit.getWires()) {

                if (wire.getSource() != component) {
                    continue;
                }

                Gate destination =
                    wire.getDestination();

                if (!allInputsReady(destination, values)) {
                    continue;
                }

                boolean destinationValue =
                    destination.evaluate(values);

                long eventTime =
                    event.getTimeNs()
                    + destination.getPropagationDelayNs();

                eventQueue.add(
                    new TimingEvent(
                        eventTime,
                        destination,
                        destinationValue
                    )
                );
            }
        }

        return waveforms;
    }

    private boolean allInputsReady(
        Gate gate,
        Map<Component, Boolean> values
    ) {
        for (int i = 0; i < gate.getInputCount(); i++) {

            Component input =
                gate.getInput(i);

            if (!values.containsKey(input)) {
                return false;
            }
        }

        return true;
    }
}