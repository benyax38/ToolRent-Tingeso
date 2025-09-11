package com.example.demo.Controller;

import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Service.ToolCatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogs")
public class ToolCatalogController {

    private final ToolCatalogService toolCatalogService;
    @Autowired
    public ToolCatalogController(ToolCatalogService toolCatalogService) { this.toolCatalogService = toolCatalogService; }

    @GetMapping
    public ResponseEntity<List<ToolCatalogEntity>> getAllCatalog() {
        List<ToolCatalogEntity> catalogList = toolCatalogService.getAllCatalogs();
        return ResponseEntity.ok(catalogList);
    }

    @PostMapping
    public ResponseEntity<ToolCatalogEntity> createCatalog(@Valid @RequestBody ToolCatalogEntity catalog) {
        ToolCatalogEntity createdCatalog = toolCatalogService.createCatalogs(catalog);
        return ResponseEntity.ok(createdCatalog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCatalogById(@PathVariable Long id) {
        try {
            toolCatalogService.deleteCatalogsById(id);
            return ResponseEntity.ok("Herramienta eliminada del catálogo");
        } catch (IllegalArgumentException errorDeleteCatalogById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteCatalogById.getMessage());
        }
    }
}
