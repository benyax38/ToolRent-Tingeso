package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.Service.ClientService.ClientStatus;

import java.util.ArrayList;
import java.util.List;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "clients")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId; //El tipo de dato Long permite que hibernate distinga entre 0 y null

    @Column(name = "client_first_name", length = 40)
    private String clientFirstName;

    @Column(name = "client_last_name", length = 40)
    private String clientLastName;

    @Column(name = "client_rut", length = 15)
    private String clientRUT;

    @Column(name = "client_phone", length = 15)
    private String clientPhone;

    @Column(name = "client_email", length = 60)
    private String clientEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_state", length = 20)
    private ClientStatus clientState;

    @OneToMany(mappedBy = "clients", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanEntity> loans = new ArrayList<>();

    @OneToMany(mappedBy = "clients", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KardexEntity> kardexes = new ArrayList<>();
}
