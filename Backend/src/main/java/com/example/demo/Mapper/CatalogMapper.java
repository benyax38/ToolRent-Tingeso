package com.example.demo.Mapper;

import com.example.demo.DTOs.ToolCatalogDTO;
import com.example.demo.Entity.ToolCatalogEntity;

public class CatalogMapper {

    public static ToolCatalogDTO toDto(ToolCatalogEntity entity) {
        if (entity == null) return null;

        return ToolCatalogDTO.builder()
                .toolCatalogId(entity.getToolCatalogId())
                .toolName(entity.getToolName())
                .toolCategory(entity.getToolCategory())
                .dailyRentalValue(entity.getDailyRentalValue())
                .replacementValue(entity.getReplacementValue())
                .description(entity.getDescription())
                .availableUnits(entity.getAvailableUnits())
                .build();
    }

    public static ToolCatalogEntity toEntity(ToolCatalogDTO dto) {
        if (dto == null) return null;

        return ToolCatalogEntity.builder()
                .toolCatalogId(dto.getToolCatalogId())
                .toolName(dto.getToolName())
                .toolCategory(dto.getToolCategory())
                .dailyRentalValue(dto.getDailyRentalValue())
                .replacementValue(dto.getReplacementValue())
                .description(dto.getDescription())
                .availableUnits(dto.getAvailableUnits())
                .build();
    }
}

