package com.circuitsim;

import com.circuitsim.api.ConnectionRequest;
import com.circuitsim.api.GateRequest;
import com.circuitsim.api.InputRequest;
import com.circuitsim.api.SimulationRequest;
import com.circuitsim.api.SimulationResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SimulationController {

    @PostMapping("/simulate")
    public SimulationResponse simulate(
        @RequestBody SimulationRequest request
    ) {
        Circuit circuit = new Circuit("API Circuit");

        Map<String, Component> components = new HashMap<>();
        Map<String, Gate> gates = new HashMap<>();

        for (InputRequest inputRequest : request.inputs()) {
            Input input = new Input(
                inputRequest.name(),
                inputRequest.value()
            );

            circuit.addComponent(input);
            components.put(input.getName(), input);
        }

        for (GateRequest gateRequest : request.gates()) {
            Gate gate = createGate(
                gateRequest.name(),
                gateRequest.type()
            );

            circuit.addComponent(gate);

            components.put(gate.getName(), gate);
            gates.put(gate.getName(), gate);
        }

        for (ConnectionRequest connection : request.connections()) {
            Component source =
                components.get(connection.source());

            Gate destination =
                gates.get(connection.destination());

            if (source == null) {
                throw new IllegalArgumentException(
                    "Unknown source component: "
                    + connection.source()
                );
            }

            if (destination == null) {
                throw new IllegalArgumentException(
                    "Unknown destination gate: "
                    + connection.destination()
                );
            }

            circuit.connect(
                source,
                destination,
                connection.inputIndex()
            );
        }

        CircuitSimulator simulator = new CircuitSimulator();

        Map<Component, Boolean> simulationResults =
            simulator.simulate(circuit);

        Map<String, Boolean> outputs =
            new LinkedHashMap<>();

        for (String outputName : request.outputs()) {
            Component output = components.get(outputName);

            if (output == null) {
                throw new IllegalArgumentException(
                    "Unknown output component: " + outputName
                );
            }

            outputs.put(
                outputName,
                simulationResults.get(output)
            );
        }

        return new SimulationResponse(outputs);
    }

    private Gate createGate(String name, String type) {
        return switch (type.toUpperCase()) {
            case "AND" -> new AndGate(name);
            case "OR" -> new OrGate(name);
            case "XOR" -> new XorGate(name);
            case "NOT" -> new NotGate(name);
            case "NAND" -> new NandGate(name);
            case "NOR" -> new NorGate(name);

            default -> throw new IllegalArgumentException(
                "Unknown gate type: " + type
            );
        };
    }
}