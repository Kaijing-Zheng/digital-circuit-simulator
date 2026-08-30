package com.circuitsim.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_circuits")
public class SavedCircuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String circuitData;

    public SavedCircuit() {
    }

    public SavedCircuit(String name, String circuitData) {
        this.name = name;
        this.circuitData = circuitData;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCircuitData() {
        return circuitData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCircuitData(String circuitData) {
        this.circuitData = circuitData;
    }
}