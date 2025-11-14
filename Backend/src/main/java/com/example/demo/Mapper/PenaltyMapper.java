package com.example.demo.Mapper;

import com.example.demo.DTOs.PenaltyDTO;
import com.example.demo.Entity.PenaltyEntity;

public class PenaltyMapper {

    public static PenaltyDTO toDto(PenaltyEntity entity) {
        PenaltyDTO dto = new PenaltyDTO();

        dto.setPenaltyId(entity.getPenaltyId());
        dto.setAmount(entity.getAmount());
        dto.setReason(entity.getReason());
        dto.setDelayDays(entity.getDelayDays());
        dto.setDailyFineRate(entity.getDailyFineRate());
        dto.setRepairCharge(entity.getRepairCharge());
        dto.setPenaltyStatus(entity.getPenaltyStatus().name());

        dto.setLoanId(entity.getLoan().getLoanId());

        return dto;
    }
}
