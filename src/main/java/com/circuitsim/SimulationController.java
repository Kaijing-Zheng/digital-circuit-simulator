package com.circuitsim;

import com.circuitsim.api.ConnectionRequest;
import com.circuitsim.api.GateRequest;
import com.circuitsim.api.InputRequest;
import com.circuitsim.api.SimulationRequest;
import com.circuitsim.api.SimulationResponse;
import com.circuitsim.api.TruthTableResponse;
import com.circuitsim.api.SessionResponse;
import com.circuitsim.api.SessionSimulationRequest;
import com.circuitsim.api.TimingPointResponse;
import com.circuitsim.api.TimingSimulationResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SimulationController {

    private final SimulationSessionManager sessionManager;

    public SimulationController(
        SimulationSessionManager sessionManager
    ) {
        this.sessionManager = sessionManager;
    }

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


    @PostMapping("/truth-table")
    public TruthTableResponse generateTruthTable(
        @RequestBody SimulationRequest request
    ) {
        Circuit circuit = new Circuit("Truth Table Circuit");

        Map<String, Component> components = new HashMap<>();
        Map<String, Gate> gates = new HashMap<>();
        List<Input> inputs = new ArrayList<>();

        for (InputRequest inputRequest : request.inputs()) {
            Input input = new Input(
                inputRequest.name(),
                false
            );

            circuit.addComponent(input);

            components.put(input.getName(), input);
            inputs.add(input);
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

        List<Map<String, Boolean>> rows =
            new ArrayList<>();

        int combinations = 1 << inputs.size();

        for (
            int combination = 0;
            combination < combinations;
            combination++
        ) {

            for (int i = 0; i < inputs.size(); i++) {
                int bitPosition =
                    inputs.size() - 1 - i;

                boolean value =
                    ((combination >> bitPosition) & 1) == 1;

                inputs.get(i).setValue(value);
            }

            Map<Component, Boolean> simulationResults =
                simulator.simulate(circuit);

            Map<String, Boolean> row =
                new LinkedHashMap<>();

            for (Input input : inputs) {
                row.put(
                    input.getName(),
                    input.evaluate()
                );
            }

            for (String outputName : request.outputs()) {
                Component output =
                    components.get(outputName);

                if (output == null) {
                    throw new IllegalArgumentException(
                        "Unknown output component: "
                        + outputName
                    );
                }

                row.put(
                    outputName,
                    simulationResults.get(output)
                );
            }

            rows.add(row);
        }

        List<String> inputNames = inputs
            .stream()
            .map(Input::getName)
            .toList();

        return new TruthTableResponse(
            inputNames,
            request.outputs(),
            rows
        );
    }

    @PostMapping("/simulation-sessions")
    public SessionResponse createSimulationSession(
        @RequestBody SimulationRequest request
    ) {
        Circuit circuit = new Circuit("Simulation Session");

        Map<String, Component> components =
            new HashMap<>();

        Map<String, Gate> gates =
            new HashMap<>();

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

        String sessionId =
            sessionManager.createSession(
                circuit,
                components
            );

        return new SessionResponse(sessionId);
    }

    @PostMapping("/simulation-sessions/{sessionId}/simulate")
    public SimulationResponse simulateSession(
        @PathVariable String sessionId,
        @RequestBody SessionSimulationRequest request
    ) {
        SimulationSession session =
            sessionManager.getSession(sessionId);

        if (session == null) {
            throw new IllegalArgumentException(
                "Unknown simulation session: "
                + sessionId
            );
        }

        for (InputRequest inputRequest : request.inputs()) {
            Component component =
                session.getComponent(
                    inputRequest.name()
                );

            if (!(component instanceof Input input)) {
                throw new IllegalArgumentException(
                    "Unknown input: "
                    + inputRequest.name()
                );
            }

            input.setValue(
                inputRequest.value()
            );
        }

        CircuitSimulator simulator =
            new CircuitSimulator();

        Map<Component, Boolean> simulationResults =
            simulator.simulate(
                session.getCircuit()
            );

        Map<String, Boolean> outputs =
            new LinkedHashMap<>();

        for (String outputName : request.outputs()) {
            Component output =
                session.getComponent(outputName);

            if (output == null) {
                throw new IllegalArgumentException(
                    "Unknown output component: "
                    + outputName
                );
            }

            outputs.put(
                outputName,
                simulationResults.get(output)
            );
        }

        return new SimulationResponse(outputs);
    }


    @PostMapping("/timing-simulate")
    public TimingSimulationResponse timingSimulate(
        @RequestBody SimulationRequest request
    ) {
        Circuit circuit =
            new Circuit("Timing Simulation");

        Map<String, Component> components =
            new LinkedHashMap<>();

        Map<String, Gate> gates =
            new HashMap<>();

        for (InputRequest inputRequest : request.inputs()) {
            Input input =
                new Input(
                    inputRequest.name(),
                    inputRequest.value()
                );

            circuit.addComponent(input);

            components.put(
                input.getName(),
                input
            );
        }

        for (GateRequest gateRequest : request.gates()) {
            Gate gate =
                createGate(
                    gateRequest.name(),
                    gateRequest.type()
                );

            circuit.addComponent(gate);

            components.put(
                gate.getName(),
                gate
            );

            gates.put(
                gate.getName(),
                gate
            );
        }

        for (
            ConnectionRequest connection :
            request.connections()
        ) {
            Component source =
                components.get(
                    connection.source()
                );

            Gate destination =
                gates.get(
                    connection.destination()
                );

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

        TimingSimulator simulator =
            new TimingSimulator();

        Map<Component, List<WaveformPoint>> results =
            simulator.simulate(circuit);

        Map<String, List<TimingPointResponse>> waveforms =
            new LinkedHashMap<>();

        for (
            Map.Entry<Component, List<WaveformPoint>> entry :
            results.entrySet()
        ) {
            List<TimingPointResponse> points =
                entry.getValue()
                    .stream()
                    .map(point ->
                        new TimingPointResponse(
                            point.getTimeNs(),
                            point.getValue()
                        )
                    )
                    .toList();

            waveforms.put(
                entry.getKey().getName(),
                points
            );
        }

        return new TimingSimulationResponse(
            waveforms
        );
    }

    private Gate createGate(String name, String type) {
        return switch (type.toUpperCase()) {
            case "AND" -> new AndGate(name);
            case "OR" -> new OrGate(name);
            case "XOR" -> new XorGate(name);
            case "XNOR" -> new XnorGate(name);
            case "NOT" -> new NotGate(name);
            case "NAND" -> new NandGate(name);
            case "NOR" -> new NorGate(name);
            case "DFF" -> new DFlipFlop(name);

            default -> throw new IllegalArgumentException(
                "Unknown gate type: " + type
            );
        };
    }
}