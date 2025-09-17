package com.example.demo.Controller;

import com.example.demo.DTOs.ToolCatalogDTO;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public ToolCatalogController(ToolCatalogService toolCatalogService, UserRepository userRepository) {
        this.toolCatalogService = toolCatalogService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<ToolCatalogDTO>> getAllCatalog() {
        List<ToolCatalogDTO> catalogList = toolCatalogService.getAllCatalogsDTO();
        return ResponseEntity.ok(catalogList);
    }

    @PostMapping
    public ResponseEntity<ToolCatalogEntity> createCatalog(
            @Valid @RequestBody ToolCatalogEntity catalog,
            @RequestParam Long userId) {

        //Busca el usuario que ejecuta la acción
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ToolCatalogEntity createdCatalog = toolCatalogService.createCatalogs(catalog, user);
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
