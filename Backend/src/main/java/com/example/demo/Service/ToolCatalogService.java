package com.example.demo.Service;

import com.example.demo.DTOs.KardexCreateDTO;
import com.example.demo.DTOs.ToolCatalogAddUnitsDTO;
import com.example.demo.DTOs.ToolCatalogDTO;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Mapper.CatalogMapper;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolCatalogService {

    private final ToolCatalogRepository toolCatalogRepository;
    private final ToolRepository toolRepository;
    private final KardexService kardexService;
    private final UserRepository userRepository;

    @Autowired
    public ToolCatalogService(ToolCatalogRepository toolCatalogRepository, ToolRepository toolRepository, KardexService kardexService, UserRepository userRepository) {
        this.toolCatalogRepository = toolCatalogRepository;
        this.toolRepository = toolRepository;
        this.kardexService = kardexService;
        this.userRepository = userRepository;
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

        // Registrar movimiento en el kardex
        KardexCreateDTO dto = KardexCreateDTO.builder()
                .type(KardexService.KardexMovementType.INGRESO)
                .affectedAmount(savedToolCatalog.getAvailableUnits())
                .details("Nueva herramienta ingresada al sistema")
                .clientId(null)
                .loanId(null)
                .toolId(null)
                .catalogId(savedToolCatalog.getToolCatalogId())
                .userId(user.getUserId())
                .build();

        kardexService.registerMovement(dto);

        return savedToolCatalog;
    }

    @Transactional
    public ToolCatalogDTO addUnitsToCatalog(Long catalogId, ToolCatalogAddUnitsDTO dto) {

        ToolCatalogEntity catalog = toolCatalogRepository.findById(catalogId)
                .orElseThrow(() -> new RuntimeException("Catálogo no encontrado"));

        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (dto.getUnits() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Crear nuevas herramientas fisicas
        for (int i = 0; i < dto.getUnits(); i++) {
            ToolEntity tool = new ToolEntity();
            tool.setTool_catalogs(catalog);
            tool.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
            toolRepository.save(tool);
        }

        // Actualizar stock disponible
        catalog.setAvailableUnits(catalog.getAvailableUnits() + dto.getUnits());
        toolCatalogRepository.save(catalog);

        // Registrar movimiento en kardex
        kardexService.registerMovement(
                KardexCreateDTO.builder()
                        .type(KardexService.KardexMovementType.INGRESO)
                        .affectedAmount(dto.getUnits())
                        .details("Ingreso de nuevas herramientas a un catálogo existente")
                        .toolId(null)
                        .catalogId(catalog.getToolCatalogId())
                        .loanId(null)
                        .userId(user.getUserId())
                        .clientId(null)
                        .build()
        );

        return CatalogMapper.toDto(catalog);
    }


    public void deleteCatalogsById(Long id) {
        if (!toolCatalogRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta en el catálogo con id: " + id);
        }
        toolCatalogRepository.deleteById(id);
    }
}
