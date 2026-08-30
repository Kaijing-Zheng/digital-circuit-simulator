package com.circuitsim.api;

import java.util.List;
import java.util.Map;

public record TimingSimulationResponse(
    Map<String, List<TimingPointResponse>> waveforms
) {}