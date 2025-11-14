package com.example.demo.DTOs;

import lombok.Data;

@Data
public class PenaltyDTO {

    private Long penaltyId;
    private Double amount;
    private String reason;
    private int delayDays;
    private Double dailyFineRate;
    private Double repairCharge;
    private String penaltyStatus;

    private Long loanId;
}
