package com.example.demo.Service;

import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Repository.ToolCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolCatalogService {

    private final ToolCatalogRepository toolCatalogRepository;

    @Autowired
    public ToolCatalogService(ToolCatalogRepository toolCatalogRepository) { this.toolCatalogRepository = toolCatalogRepository; }

    public List<ToolCatalogEntity> getAllCatalogs() {
        return toolCatalogRepository.findAll();
    }

    public ToolCatalogEntity createCatalogs(ToolCatalogEntity catalog) {
        return toolCatalogRepository.save(catalog);
    }

    public void deleteCatalogsById(Long id) {
        if (!toolCatalogRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta en el catálogo con id: " + id);
        }
        toolCatalogRepository.deleteById(id);
    }
}
