package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

/*
    * El proposito de este DTO es mostrar los atributos
    * de los prestamos creados de manera limpia y ordenada
*/

public class LoanResponseDTO {

    private Long loanId;
    private LocalDateTime deliveryDate;
    private LocalDateTime deadline;
    private LocalDateTime returnDate;
    private Double rentalAmount;
    private String loanStatus;
    private Long clientId;
    private Long userId;
    private Long toolId;
}
