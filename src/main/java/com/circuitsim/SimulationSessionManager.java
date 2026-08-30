package com.circuitsim;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SimulationSessionManager {

    private final Map<String, SimulationSession> sessions =
        new HashMap<>();

    public String createSession(
        Circuit circuit,
        Map<String, Component> components
    ) {
        String sessionId = UUID.randomUUID().toString();

        SimulationSession session =
            new SimulationSession(
                circuit,
                components
            );

        sessions.put(sessionId, session);

        return sessionId;
    }

    public SimulationSession getSession(
        String sessionId
    ) {
        return sessions.get(sessionId);
    }

    public void deleteSession(
        String sessionId
    ) {
        sessions.remove(sessionId);
    }
}