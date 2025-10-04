package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolCatalogDTO {

    private Long toolCatalogId;
    private String toolName;
    private String toolCategory;
    private Double rentalValue;
    private Double replacementValue;
    private String description;
    private int availableUnits;

}
