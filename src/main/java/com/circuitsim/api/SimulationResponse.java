package com.circuitsim.api;

import java.util.Map;

public record SimulationResponse(
    Map<String, Boolean> outputs
) {
}