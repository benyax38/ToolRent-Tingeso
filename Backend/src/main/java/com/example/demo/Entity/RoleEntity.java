package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "roles")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role", length = 25)
    private String roleName;

    @OneToMany(mappedBy = "roles", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> users = new ArrayList<>();
}
