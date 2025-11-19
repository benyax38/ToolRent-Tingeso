package com.example.demo.Controller;

import com.example.demo.DTOs.ToolDTO;
import com.example.demo.DTOs.ToolEvaluationDTO;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Service.ToolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final ToolService toolService;
    @Autowired
    public ToolController(ToolService toolService) { this.toolService = toolService; }

    @GetMapping
    public ResponseEntity<List<ToolDTO>> getAllTool() {
        List<ToolDTO> toolList = toolService.getAllToolsDTO();
        return ResponseEntity.ok(toolList);
    }

    @PostMapping
    public ResponseEntity<ToolEntity> createTool(@Valid @RequestBody ToolEntity tool) {
        ToolEntity createdTool = toolService.createTools(tool);
        return ResponseEntity.ok(createdTool);
    }

    @PostMapping("/evaluation/{toolId}/user/{userId}")
    public ResponseEntity<?> evaluateTool(
            @PathVariable Long toolId,
            @PathVariable Long userId,
            @RequestBody ToolEvaluationDTO request
    ) {
        ToolEntity updated = toolService.evaluateTools(toolId, userId, request);
        return ResponseEntity.ok("La herramienta #" + toolId + " ha sido evaluada correctamente. Nuevo estado: " + updated.getCurrentToolState());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteToolById(@PathVariable Long id) {
        try {
            toolService.deleteToolsById(id);
            return ResponseEntity.ok("Herramienta eliminada correctamente");
        } catch (IllegalArgumentException errorDeleteToolById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteToolById.getMessage());
        }
    }




}
