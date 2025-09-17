package com.example.demo.Service;

import com.example.demo.DTOs.ToolCatalogDTO;
import com.example.demo.Entity.KardexEntity;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.KardexRepository;
import com.example.demo.Repository.ToolCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ToolCatalogService {

    private final ToolCatalogRepository toolCatalogRepository;
    private final KardexRepository kardexRepository;

    @Autowired
    public ToolCatalogService(ToolCatalogRepository toolCatalogRepository, KardexRepository kardexRepository) {
        this.toolCatalogRepository = toolCatalogRepository;
        this.kardexRepository = kardexRepository;
    }

    public List<ToolCatalogDTO> getAllCatalogsDTO() {
        return toolCatalogRepository.findAll().stream()
                .map(catalog -> new ToolCatalogDTO(
                        catalog.getToolCatalogId(),
                        catalog.getToolName(),
                        catalog.getToolCategory(),
                        catalog.getReplacementValue()
                ))
                .toList();
    }

    public ToolCatalogEntity createCatalogs(ToolCatalogEntity catalog, UserEntity user) {
        //Guardar herramienta en catalogo
        ToolCatalogEntity savedTool = toolCatalogRepository.save(catalog);

        //Crear movimiento en kardex
        KardexEntity kardexMovement = new KardexEntity();
        kardexMovement.setType("INGRESO");
        kardexMovement.setMovementDate(LocalDateTime.now());
        kardexMovement.setAffectedAmount(savedTool.getAvailableUnits());
        kardexMovement.setDetails("Nueva herramienta ingresada al sistema");

        //Relaciones
        kardexMovement.setTool_catalogs(savedTool);
        kardexMovement.setUsers(user);
        kardexMovement.setTools(null);
        kardexMovement.setLoans(null);
        kardexMovement.setClients(null);

        //Guardar en kardex
        kardexRepository.save(kardexMovement);

        return savedTool;

    }

    public void deleteCatalogsById(Long id) {
        if (!toolCatalogRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta en el catálogo con id: " + id);
        }
        toolCatalogRepository.deleteById(id);
    }
}
