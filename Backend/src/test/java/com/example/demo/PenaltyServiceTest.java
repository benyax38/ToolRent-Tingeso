package com.example.demo;

import com.example.demo.DTOs.PenaltyDTO;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Entity.PenaltyEntity;
import com.example.demo.Repository.PenaltyRepository;
import com.example.demo.Service.PenaltyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PenaltyServiceTest {

    @Mock
    private PenaltyRepository penaltyRepository;

    @InjectMocks
    private PenaltyService penaltyService;

    // ---------------------------------------------------------
    // Test: getAllPenaltiesDTO()
    // ---------------------------------------------------------
    @Test
    void testGetAllPenaltiesDTO() {

        // Loan simulada
        LoanEntity loan1 = new LoanEntity();
        loan1.setLoanId(10L);

        LoanEntity loan2 = new LoanEntity();
        loan2.setLoanId(20L);

        // Penalty 1
        PenaltyEntity p1 = new PenaltyEntity();
        p1.setPenaltyId(1L);
        p1.setAmount(100.0);
        p1.setReason("Atraso");
        p1.setDelayDays(2);
        p1.setDailyFineRate(500.0);
        p1.setRepairCharge(0.0);
        p1.setPenaltyStatus(PenaltyService.PaymentStatus.IMPAGO);
        p1.setLoan(loan1);

        // Penalty 2
        PenaltyEntity p2 = new PenaltyEntity();
        p2.setPenaltyId(2L);
        p2.setAmount(200.0);
        p2.setReason("Daño menor");
        p2.setDelayDays(0);
        p2.setDailyFineRate(500.0);
        p2.setRepairCharge(3000.0);
        p2.setPenaltyStatus(PenaltyService.PaymentStatus.PAGADO);
        p2.setLoan(loan2);

        when(penaltyRepository.findAll()).thenReturn(List.of(p1, p2));

        // Ejecución
        List<PenaltyDTO> result = penaltyService.getAllPenaltiesDTO();

        // Validaciones
        assertEquals(2, result.size());

        PenaltyDTO dto1 = result.get(0);
        assertEquals(1L, dto1.getPenaltyId());
        assertEquals(100.0, dto1.getAmount());
        assertEquals("Atraso", dto1.getReason());
        assertEquals(2, dto1.getDelayDays());
        assertEquals(500.0, dto1.getDailyFineRate());
        assertEquals(0.0, dto1.getRepairCharge());
        assertEquals("IMPAGO", dto1.getPenaltyStatus());
        assertEquals(10L, dto1.getLoanId());

        PenaltyDTO dto2 = result.get(1);
        assertEquals(2L, dto2.getPenaltyId());
        assertEquals(200.0, dto2.getAmount());
        assertEquals("Daño menor", dto2.getReason());
        assertEquals("PAGADO", dto2.getPenaltyStatus());
        assertEquals(20L, dto2.getLoanId());
    }


    // ---------------------------------------------------------
    // Test: createPenalties()
    // ---------------------------------------------------------
    @Test
    void testCreatePenalties() {
        PenaltyEntity penalty = new PenaltyEntity();
        penalty.setPenaltyId(10L);
        penalty.setAmount(300.0);

        when(penaltyRepository.save(penalty)).thenReturn(penalty);

        PenaltyEntity saved = penaltyService.createPenalties(penalty);

        assertNotNull(saved);
        assertEquals(10L, saved.getPenaltyId());
        verify(penaltyRepository, times(1)).save(penalty);
    }

    // ---------------------------------------------------------
    // Test: deletePenaltiesById() - caso OK
    // ---------------------------------------------------------
    @Test
    void testDeletePenaltyByIdSuccess() {
        Long id = 5L;

        when(penaltyRepository.existsById(id)).thenReturn(true);

        penaltyService.deletePenaltiesById(id);

        verify(penaltyRepository, times(1)).deleteById(id);
    }

    // ---------------------------------------------------------
    // Test: deletePenaltiesById() - caso NO existe
    // ---------------------------------------------------------
    @Test
    void testDeletePenaltyByIdNotFound() {
        Long id = 99L;

        when(penaltyRepository.existsById(id)).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                penaltyService.deletePenaltiesById(id)
        );

        assertEquals("No existe multa con id: 99", ex.getMessage());
        verify(penaltyRepository, never()).deleteById(id);
    }
}

