package com.example.demo.Repository;

import com.example.demo.Entity.PenaltyEntity;
import com.example.demo.Service.PenaltyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepository extends JpaRepository<PenaltyEntity,Long> {

    // Consulta que comprueba si el cliente del id ingresado tiene deudas sin pagar
    boolean existsByLoans_Clients_ClientIdAndPenaltyStatus(
            Long clientId,
            PenaltyService.PaymentStatus penaltyStatus
    );
}
