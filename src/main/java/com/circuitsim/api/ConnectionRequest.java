package com.circuitsim.api;

public record ConnectionRequest(
    String source,
    String destination,
    int inputIndex
) {
}