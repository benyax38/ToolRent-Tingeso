package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "loans")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId; //El tipo de dato Long permite que hibernate distinga entre 0 y null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deadline")
    private LocalDateTime deadline;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "loan_state", length = 20)
    private String loanState;

    @OneToMany(mappedBy = "loans", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PenaltyEntity> penalties = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity clients;

    @OneToMany(mappedBy = "loans", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KardexEntity> kardexes = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "tool_id", nullable = false)
    private ToolEntity tools;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity users;
}
