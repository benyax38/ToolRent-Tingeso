package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolCatalogDTO {

    private Long toolId;
    private String toolName;
    private String toolCategory;
    private Double replacementValue;

}
