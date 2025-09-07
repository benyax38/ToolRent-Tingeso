package com.example.demo.Service;

import com.example.demo.Entity.ToolEntity;
import com.example.demo.Repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ToolEntity> getAllTools() {
        return toolRepository.findAll();
    }

    public ToolEntity createTools(ToolEntity tool) {
        tool.setInitialState(ToolStatus.DISPONIBLE);
        tool.setCurrentState(ToolStatus.DISPONIBLE);
        return toolRepository.save(tool);
    }

    public void deleteToolsById(Long id) {
        if (!toolRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta con id: " + id);
        }
        toolRepository.deleteById(id);
    }


}
