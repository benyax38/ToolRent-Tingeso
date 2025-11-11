package com.example.demo.Mapper;

import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.Entity.LoanEntity;

public class LoanMapper {

    public static LoanResponseDTO toDto(LoanEntity loan) {
        if (loan == null) return null;

        return LoanResponseDTO.builder()
                .loanId(loan.getLoanId())
                .deliveryDate(loan.getDeliveryDate())
                .deadline(loan.getDeadline())
                .returnDate(loan.getReturnDate())
                .rentalAmount(loan.getRentalAmount())
                .loanStatus(loan.getLoanStatus().name())
                .clientId(loan.getClients() != null ? loan.getClients().getClientId() : null)
                .userId(loan.getUsers() != null ? loan.getUsers().getUserId() : null)
                .toolId(loan.getTools() != null ? loan.getTools().getToolId() : null)
                .build();
    }
}
