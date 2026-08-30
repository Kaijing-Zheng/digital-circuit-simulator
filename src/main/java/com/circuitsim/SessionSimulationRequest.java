package com.circuitsim.api;

import java.util.List;

public record SessionSimulationRequest(
    List<InputRequest> inputs,
    List<String> outputs
) {
}