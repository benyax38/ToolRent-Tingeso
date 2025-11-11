package com.example.demo.Service;

import com.example.demo.Entity.PenaltyConfigEntity;
import com.example.demo.Repository.PenaltyConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PenaltyConfigService {

    private final PenaltyConfigRepository repository;

    public PenaltyConfigEntity createConfigs(PenaltyConfigEntity config) {
        // Valida si existe config --> solo puede existir una entidad unica
        if (repository.count() > 0) {
            throw new RuntimeException("Ya existe una configuración.");
        }
        return repository.save(config);
    }

    public PenaltyConfigEntity getConfigs() {
        return repository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe configuración"));
    }

    public PenaltyConfigEntity updateDailyFinesRate(Double newRate) {
        PenaltyConfigEntity config = getConfigs();
        config.setDailyFineRate(newRate);
        return repository.save(config);
    }

    public PenaltyConfigEntity updateRepairCharges(Double newCharge) {
        PenaltyConfigEntity config = getConfigs();
        config.setRepairCharge(newCharge);
        return repository.save(config);
    }
}
