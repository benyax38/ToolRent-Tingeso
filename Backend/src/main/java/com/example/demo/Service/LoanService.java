package com.example.demo.Service;

import com.example.demo.Entity.LoanEntity;
import com.example.demo.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) { this.loanRepository = loanRepository; }

    public List<LoanEntity> getAllLoans() {
        return loanRepository.findAll();
    }

    public LoanEntity createLoans(LoanEntity loan) {
        return loanRepository.save(loan);
    }

    public void deleteLoansById(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe préstamo con id: " + id);
        }
        loanRepository.deleteById(id);
    }
}
