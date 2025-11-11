package com.example.demo.Entity;

import com.example.demo.Service.PenaltyService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "penalties")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class PenaltyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_id")
    private Long penaltyId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "reason", length = 20)
    private String reason;

    @Column(name = "delay_days")
    private int delayDays;

    @Column(name = "daily_fine_rate")
    private Double dailyFineRate;

    @Column(name = "repair_charge")
    private Double repairCharge;

    @Enumerated(EnumType.STRING)
    @Column(name = "penalty_state", length = 20)
    private PenaltyService.PaymentStatus penaltyStatus; //Gestion de estado

    @OneToOne
    @JoinColumn(name = "loan_id", nullable = false, unique = true)
    private LoanEntity loan;
}
