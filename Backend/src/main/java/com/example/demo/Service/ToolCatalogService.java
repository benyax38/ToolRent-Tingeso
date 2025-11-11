package com.example.demo.Service;

import com.example.demo.DTOs.ToolCatalogDTO;
import com.example.demo.Entity.KardexEntity;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.KardexRepository;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Mapper.CatalogMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ToolCatalogService {

    private final ToolCatalogRepository toolCatalogRepository;
    private final KardexRepository kardexRepository;
    private final ToolRepository toolRepository;

    @Autowired
    public ToolCatalogService(ToolCatalogRepository toolCatalogRepository, KardexRepository kardexRepository, ToolRepository toolRepository) {
        this.toolCatalogRepository = toolCatalogRepository;
        this.kardexRepository = kardexRepository;
        this.toolRepository = toolRepository;
    }

    public List<ToolCatalogDTO> getAllCatalogsDTO() {
        return toolCatalogRepository.findAll().stream()
                .map(catalog -> new ToolCatalogDTO(
                        catalog.getToolCatalogId(),
                        catalog.getToolName(),
                        catalog.getToolCategory(),
                        catalog.getDailyRentalValue(),
                        catalog.getReplacementValue(),
                        catalog.getDescription(),
                        catalog.getAvailableUnits()
                ))
                .toList();
    }

    public ToolCatalogDTO getCatalogsById(Long id) {
        ToolCatalogEntity entity = toolCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada en el catálogo. Id: " + id));
        return CatalogMapper.toDto(entity);
    }

    public List<ToolCatalogDTO> getCatalogsByName(String toolName) {
        var entities = toolCatalogRepository.findByToolNameContainingIgnoreCase(toolName);
        return entities.stream()
                .map(CatalogMapper::toDto)
                .toList();
    }

    @Transactional
    public ToolCatalogEntity createCatalogs(ToolCatalogEntity catalog, UserEntity user) {
        // Guardar herramienta en catálogo
        ToolCatalogEntity savedToolCatalog = toolCatalogRepository.save(catalog);

        // Crear las instancias físicas de Tool
        for (int i = 0; i < savedToolCatalog.getAvailableUnits(); i++) {
            ToolEntity tool = new ToolEntity();
            tool.setTool_catalogs(savedToolCatalog);
            tool.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
            toolRepository.save(tool);
        }

        // Crear movimiento en kardex
        KardexEntity kardexMovement = new KardexEntity();
        kardexMovement.setType("INGRESO");
        kardexMovement.setMovementDate(LocalDateTime.now());
        kardexMovement.setAffectedAmount(savedToolCatalog.getAvailableUnits());
        kardexMovement.setDetails("Nueva herramienta ingresada al sistema");

        // Relaciones
        kardexMovement.setTool_catalogs(savedToolCatalog);
        kardexMovement.setUsers(user);
        kardexMovement.setTools(null);
        kardexMovement.setLoans(null);
        kardexMovement.setClients(null);

        // Guardar en kardex
        kardexRepository.save(kardexMovement);

        return savedToolCatalog;
    }


    public void deleteCatalogsById(Long id) {
        if (!toolCatalogRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta en el catálogo con id: " + id);
        }
        toolCatalogRepository.deleteById(id);
    }
}
