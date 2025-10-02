package com.example.demo.Controller;

import com.example.demo.Entity.PenaltyEntity;
import com.example.demo.Service.PenaltyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/penalties")
public class PenaltyController {

    private final PenaltyService penaltyService;
    @Autowired
    public PenaltyController(PenaltyService penaltyService) { this.penaltyService = penaltyService; }

    @GetMapping
    public ResponseEntity<List<PenaltyEntity>> getAllPenalty() {
        List<PenaltyEntity> penaltyList = penaltyService.getAllPenalties();
        return ResponseEntity.ok(penaltyList);
    }

    @PostMapping
    public ResponseEntity<PenaltyEntity> createPenalty(@Valid @RequestBody PenaltyEntity penalty) {
        PenaltyEntity createdPenalty = penaltyService.createPenalties(penalty);
        return ResponseEntity.ok(createdPenalty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePenaltyById(@PathVariable Long id) {
        try {
            penaltyService.deletePenaltiesById(id);
            return ResponseEntity.ok("Multa eliminada correctamente");
        } catch (IllegalArgumentException errorDeletePenaltyById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeletePenaltyById.getMessage());
        }
    }
}
