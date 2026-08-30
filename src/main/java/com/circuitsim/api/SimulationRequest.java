package com.circuitsim.api;

import java.util.List;

public record SimulationRequest(
    List<InputRequest> inputs,
    List<GateRequest> gates,
    List<ConnectionRequest> connections,
    List<String> outputs
) {
}