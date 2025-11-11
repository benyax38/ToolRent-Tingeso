package com.example.demo.Controller;

import com.example.demo.Entity.PenaltyConfigEntity;
import com.example.demo.Service.PenaltyConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/penalty-config")
@RequiredArgsConstructor
public class PenaltyConfigController {

    private final PenaltyConfigService service;

    @PostMapping
    public ResponseEntity<PenaltyConfigEntity> createConfig(@RequestBody PenaltyConfigEntity config) {
        return ResponseEntity.ok(service.createConfigs(config));
    }

    @GetMapping
    public ResponseEntity<PenaltyConfigEntity> getConfig() {
        return ResponseEntity.ok(service.getConfigs());
    }

    @PatchMapping("/daily-fine")
    public ResponseEntity<PenaltyConfigEntity> updateDailyFineRate(@RequestBody Double rate) {
        return ResponseEntity.ok(service.updateDailyFinesRate(rate));
    }

    @PatchMapping("/repair-charge")
    public ResponseEntity<PenaltyConfigEntity> updateRepairCharge(@RequestBody Double charge) {
        return ResponseEntity.ok(service.updateRepairCharges(charge));
    }
}
