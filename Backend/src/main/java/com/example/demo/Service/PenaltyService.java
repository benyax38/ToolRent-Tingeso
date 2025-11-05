package com.example.demo.Service;

import com.example.demo.Entity.PenaltyEntity;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public PenaltyService(PenaltyRepository penaltyRepository, LoanRepository loanRepository) { this.penaltyRepository = penaltyRepository;
        this.loanRepository = loanRepository;
    }

    public enum PaymentStatus {
        PAGADO,
        IMPAGO
    }

    public List<PenaltyEntity> getAllPenalties() {
        return penaltyRepository.findAll();
    }

    public PenaltyEntity createPenalties(PenaltyEntity penalty) {
        return penaltyRepository.save(penalty);
    }

    public void deletePenaltiesById(Long id) {
        if (!penaltyRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe multa con id: " + id);
        }
        penaltyRepository.deleteById(id);
    }
}
