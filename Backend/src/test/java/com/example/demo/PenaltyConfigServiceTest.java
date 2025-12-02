package com.example.demo;

import com.example.demo.Entity.PenaltyConfigEntity;
import com.example.demo.Repository.PenaltyConfigRepository;
import com.example.demo.Service.PenaltyConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PenaltyConfigServiceTest {

    @Mock
    private PenaltyConfigRepository repository;

    @InjectMocks
    private PenaltyConfigService service;

    private PenaltyConfigEntity config;

    @BeforeEach
    void setUp() {
        config = new PenaltyConfigEntity();
        config.setPenaltyConfigId(1L);
        config.setDailyFineRate(10.0);
        config.setRepairCharge(50.0);
    }

    // -----------------------------------------
    // createConfigs
    // -----------------------------------------
    @Test
    void createConfigs_success() {
        when(repository.count()).thenReturn(0L);
        when(repository.save(config)).thenReturn(config);

        PenaltyConfigEntity result = service.createConfigs(config);

        assertNotNull(result);
        assertEquals(config, result);
        verify(repository).save(config);
    }

    @Test
    void createConfigs_whenAlreadyExists_throwsException() {
        when(repository.count()).thenReturn(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createConfigs(config));

        assertEquals("Ya existe una configuración.", ex.getMessage());
        verify(repository, never()).save(any());
    }

    // -----------------------------------------
    // getConfigs
    // -----------------------------------------
    @Test
    void getConfigs_returnsFirstConfig() {
        when(repository.findAll()).thenReturn(List.of(config));

        PenaltyConfigEntity result = service.getConfigs();

        assertNotNull(result);
        assertEquals(config, result);
    }

    @Test
    void getConfigs_returnsNullWhenEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        PenaltyConfigEntity result = service.getConfigs();

        assertNull(result);
    }

    // -----------------------------------------
    // updateDailyFinesRate
    // -----------------------------------------
    @Test
    void updateDailyFinesRate_success() {
        when(repository.findAll()).thenReturn(List.of(config));
        when(repository.save(any())).thenReturn(config);

        PenaltyConfigEntity result = service.updateDailyFinesRate(25.0);

        assertEquals(25.0, result.getDailyFineRate());
        verify(repository).save(config);
    }

    // -----------------------------------------
    // updateRepairCharges
    // -----------------------------------------
    @Test
    void updateRepairCharges_success() {
        when(repository.findAll()).thenReturn(List.of(config));
        when(repository.save(any())).thenReturn(config);

        PenaltyConfigEntity result = service.updateRepairCharges(90.0);

        assertEquals(90.0, result.getRepairCharge());
        verify(repository).save(config);
    }
}
