package com.example.demo.Controller;

import com.example.demo.DTOs.KardexDTO;
import com.example.demo.Entity.KardexEntity;
import com.example.demo.Service.KardexService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kardex")
public class KardexController {

    private final KardexService kardexService;
    @Autowired
    public KardexController(KardexService kardexService) { this.kardexService = kardexService; }

    @GetMapping
    public ResponseEntity<List<KardexDTO>> getAllKardex() {
        List<KardexDTO> kardexList = kardexService.getAllKardexesDTO();
        return ResponseEntity.ok(kardexList);
    }

    @PostMapping
    public ResponseEntity<KardexEntity> createKardex(@Valid @RequestBody KardexEntity kardex) {
        KardexEntity createdKardex = kardexService.createKardexes(kardex);
        return ResponseEntity.ok(createdKardex);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKardexById(@PathVariable Long id) {
        try {
            kardexService.deleteKardexesById(id);
            return ResponseEntity.ok("Kardex eliminado correctamente");
        } catch (IllegalArgumentException errorDeleteKardexById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteKardexById.getMessage());
        }
    }
}
