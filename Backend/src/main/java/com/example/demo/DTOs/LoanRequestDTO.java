package com.example.demo.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data

/*
    * El objetivo de este DTO es dar la plantilla
    * del objeto JSON que se ingresa para crear un
    * prestamo
*/

public class LoanRequestDTO {
    private LocalDateTime deliveryDate;
    private LocalDateTime deadline;
    private LocalDateTime returnDate;
    private String loanStatus;
    private Long clientId;
    private Long userId;
    private Long toolId;
}
