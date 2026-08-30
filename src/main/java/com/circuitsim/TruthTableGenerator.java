package com.circuitsim;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TruthTableGenerator {

    public List<Map<String, Boolean>> generate(
        Circuit circuit,
        List<Input> inputs,
        List<Component> outputs
    ) {
        List<Map<String, Boolean>> table = new ArrayList<>();

        int numberOfCombinations = 1 << inputs.size();

        CircuitSimulator simulator = new CircuitSimulator();

        for (int combination = 0;
             combination < numberOfCombinations;
             combination++) {

            for (int i = 0; i < inputs.size(); i++) {
                boolean value =
                    ((combination >> i) & 1) == 1;

                inputs.get(i).setValue(value);
            }

            Map<Component, Boolean> simulationResults =
                simulator.simulate(circuit);

            Map<String, Boolean> row =
                new LinkedHashMap<>();

            for (Input input : inputs) {
                row.put(
                    input.getName(),
                    simulationResults.get(input)
                );
            }

            for (Component output : outputs) {
                row.put(
                    output.getName(),
                    simulationResults.get(output)
                );
            }

            table.add(row);
        }

        return table;
    }
}