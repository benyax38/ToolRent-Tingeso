package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.Service.ClientService.ClientStatus;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "clients")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder

public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //El tipo de dato Long permite que hibernate distinga entre 0 y null

    private String name;
    private String rut;
    private String email;

    @Enumerated(EnumType.STRING)
    private ClientStatus state;
}
