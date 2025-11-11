package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "penalty_config")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden
public class PenaltyConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PenaltyConfigId;

    private Double dailyFineRate;

    private Double repairCharge;
}
