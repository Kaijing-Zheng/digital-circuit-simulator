package com.circuitsim.persistence;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/circuits")
public class SavedCircuitController {

    private final SavedCircuitRepository repository;

    public SavedCircuitController(SavedCircuitRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public SavedCircuit saveCircuit(@RequestBody SavedCircuit circuit) {
        return repository.save(circuit);
    }

    @GetMapping
    public List<SavedCircuit> getAllCircuits() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavedCircuit> getCircuit(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}