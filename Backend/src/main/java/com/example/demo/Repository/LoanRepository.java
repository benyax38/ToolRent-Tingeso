package com.example.demo.Repository;

import com.example.demo.Entity.LoanEntity;
import com.example.demo.Service.LoanService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity,Long> {

    // Consulta si es que el cliente asociado al id tiene prestamos vencidos
    boolean existsByClients_ClientIdAndLoanStatus(Long clientId, LoanService.LoanStatus loanStatus);
}
