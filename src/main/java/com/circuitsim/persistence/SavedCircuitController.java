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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuit(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavedCircuit> updateCircuit(
            @PathVariable Long id,
            @RequestBody SavedCircuit updatedCircuit) {

        return repository.findById(id)
                .map(existingCircuit -> {
                    existingCircuit.setName(updatedCircuit.getName());
                    existingCircuit.setCircuitData(updatedCircuit.getCircuitData());

                    SavedCircuit saved =
                            repository.save(existingCircuit);

                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}