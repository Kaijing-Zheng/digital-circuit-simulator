package com.circuitsim.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedCircuitRepository
        extends JpaRepository<SavedCircuit, Long> {
}