package com.example.demo.Controller;

import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.DTOs.LoanRequestDTO;
import com.example.demo.DTOs.ReturnLoanRequestDTO;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Mapper.LoanMapper;
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
    public ResponseEntity<List<LoanResponseDTO>> getAllLoan() {
        List<LoanResponseDTO> loanList = loanService.getAllLoans();
        return ResponseEntity.ok(loanList);
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@Valid @RequestBody LoanRequestDTO request) {
        LoanResponseDTO createdLoan = loanService.createLoans(request);
        return ResponseEntity.ok(createdLoan);
    }

    @PatchMapping("/return/{loanId}")
    public ResponseEntity<LoanResponseDTO> returnLoan(
            @PathVariable Long loanId,
            @RequestBody ReturnLoanRequestDTO body
    ) {
        LoanEntity loan = loanService.returnLoans(loanId, body.getDamageLevel());
        return ResponseEntity.ok(LoanMapper.toDto(loan));
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
