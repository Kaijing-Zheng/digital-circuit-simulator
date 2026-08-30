package com.circuitsim.api;

public record TimingPointResponse(
    long timeNs,
    boolean value
) {}