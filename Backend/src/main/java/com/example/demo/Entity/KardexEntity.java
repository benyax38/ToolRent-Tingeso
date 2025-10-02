package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "kardexes")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class KardexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long movementId;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "movement_date")
    private LocalDateTime movementDate;

    @Column(name = "affected_amount")
    private int affectedAmount;

    @Column(name = "details", length = 256)
    private String details;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity clients;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private LoanEntity loans;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private ToolEntity tools;

    @ManyToOne
    @JoinColumn(name = "tool_catalog_id", nullable = false)
    private ToolCatalogEntity tool_catalogs;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity users;
}
