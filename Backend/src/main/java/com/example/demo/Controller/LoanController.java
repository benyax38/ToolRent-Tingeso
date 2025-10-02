package com.example.demo.Controller;

import com.example.demo.Entity.LoanEntity;
import com.example.demo.Service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    @Autowired
    public LoanController(LoanService loanService) { this.loanService = loanService; }

    @GetMapping
    public ResponseEntity<List<LoanEntity>> getAllLoan() {
        List<LoanEntity> loanList = loanService.getAllLoans();
        return ResponseEntity.ok(loanList);
    }

    @PostMapping
    public ResponseEntity<LoanEntity> createLoan(@Valid @RequestBody LoanEntity loan) {
        LoanEntity createdLoan = loanService.createLoans(loan);
        return ResponseEntity.ok(createdLoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoanById(@PathVariable Long id) {
        try {
            loanService.deleteLoansById(id);
            return ResponseEntity.ok("Préstamo eliminado correctamente");
        } catch (IllegalArgumentException errorDeleteLoanById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteLoanById.getMessage());
        }
    }
}
