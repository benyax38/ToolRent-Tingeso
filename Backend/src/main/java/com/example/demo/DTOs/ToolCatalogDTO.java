package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToolCatalogDTO {

    private Long toolCatalogId;
    private String toolName;
    private String toolCategory;
    private Double dailyRentalValue;
    private Double replacementValue;
    private String description;
    private int availableUnits;

}
