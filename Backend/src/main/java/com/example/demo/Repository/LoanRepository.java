package com.example.demo.Repository;

import com.example.demo.Entity.LoanEntity;
import com.example.demo.Service.LoanService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity,Long> {

    // Consulta si es que el cliente asociado al id tiene prestamos vencidos
    boolean existsByClients_ClientIdAndLoanStatus(Long clientId, LoanService.LoanStatus loanStatus);

    // Busca los prestamos con el estado ingresado que aún no tienen devolucion y que ya paso su fecha limite de entrega
    List<LoanEntity> findByLoanStatusAndReturnDateIsNullAndDeadlineBefore(
            LoanService.LoanStatus loanStatus,
            LocalDateTime deadline
    );

    // Cuenta los prestamos del cliente con estado "ACTIVO", "POR_PAGAR" o "VENCIDO"
    long countByClients_ClientIdAndLoanStatusIn(Long clientId, List<LoanService.LoanStatus> statuses);

}
