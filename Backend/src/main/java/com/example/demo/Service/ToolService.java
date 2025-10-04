package com.example.demo.Service;

import com.example.demo.DTOs.ToolDTO;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository) { this.toolRepository = toolRepository; }

    //Se definen los estados posibles de una herramienta
    public enum ToolStatus {
        DISPONIBLE,
        PRESTADA,
        EN_REPARACION,
        DADA_DE_BAJA
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
