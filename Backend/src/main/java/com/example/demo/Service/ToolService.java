package com.example.demo.Service;

import com.example.demo.DTOs.ToolDTO;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToolService {

    private final ToolRepository toolRepository;
    private final ToolCatalogRepository toolCatalogRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository, ToolCatalogRepository toolCatalogRepository) {
        this.toolRepository = toolRepository;
        this.toolCatalogRepository = toolCatalogRepository;
    }

    //Se definen los estados posibles de una herramienta
    public enum ToolStatus {
        DISPONIBLE,
        PRESTADA,
        EN_REPARACION,
        DADA_DE_BAJA
    }

    public ToolEntity validateAndLoanTool(Long toolId) {

        // Buscar herramienta
        ToolEntity tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        // Validar estado disponible
        if(tool.getCurrentToolState() != ToolStatus.DISPONIBLE) {
            throw new RuntimeException("No se puede crear el préstamo: la herramienta no tiene estado disponible.");
        }

        // Validar catálogo
        ToolCatalogEntity catalog = tool.getTool_catalogs();
        if (catalog == null) {
            throw new RuntimeException("No se puede crear el préstamo: la herramienta no tiene un catálogo asociado.");
        }

        // Validar stock
        if (catalog.getAvailableUnits() <= 0) {
            throw new RuntimeException("No se puede crear el préstamo: no hay stock disponible.");
        }

        // Actualizar estado
        tool.setCurrentToolState(ToolStatus.PRESTADA);

        // Reducir stock
        catalog.setAvailableUnits(catalog.getAvailableUnits() - 1);

        // Guardar cambios
        toolRepository.save(tool);
        toolCatalogRepository.save(catalog);

        return tool;
    }

    public List<ToolDTO> getAllToolsDTO() {
        List<ToolEntity> tools = toolRepository.findAll();
        return tools.stream()
                .map(ToolDTO::new) // usa el constructor que mapea desde la entidad
                .collect(Collectors.toList());
    }

    public ToolEntity createTools(ToolEntity tool) {
        tool.setCurrentToolState(ToolStatus.DISPONIBLE);
        return toolRepository.save(tool);
    }

    public void deleteToolsById(Long id) {
        if (!toolRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta con id: " + id);
        }
        toolRepository.deleteById(id);
    }


}
