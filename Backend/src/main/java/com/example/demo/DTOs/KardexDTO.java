package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KardexDTO {

    private Long movementId;
    private String movementType;
    private LocalDateTime movementDate;
    private int affectedAmount;
    private String details;
}
