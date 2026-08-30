package com.circuitsim.api;

import java.util.List;
import java.util.Map;

public record TruthTableResponse(
        List<String> inputs,
        List<String> outputs,
        List<Map<String, Boolean>> rows
) {
}