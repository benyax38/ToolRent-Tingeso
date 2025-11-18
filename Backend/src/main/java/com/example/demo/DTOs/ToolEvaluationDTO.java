package com.example.demo.DTOs;

import com.example.demo.Service.ToolService;
import lombok.Data;

@Data

/*
    * El objetivo de este DTO es dar la forma al JSON
    * que se envia una vez realizada la evaluacion final
    * del daño de una herramienta
    *
 */
public class ToolEvaluationDTO {

    private Long loanId;
    private ToolService.ToolEvaluationDecision decision;
    private String notes;
}
