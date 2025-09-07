package com.example.demo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.Service.ToolService.ToolStatus;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "tools")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder

public class ToolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //El tipo de dato Long permite que hibernate distinga entre 0 y null

    @NotBlank(message = "Se requiere ingresar un nombre")
    private String name;

    @NotBlank(message = "Se requiere ingresar una categoría")
    private String category;

    @NotNull(message = "Se requiere un valor de reposición")
    @PositiveOrZero(message = "El valor de reposición no puede ser negativo")
    private Integer replacementValue;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    private int availableUnits; //Manejo de STOCK

    //ToolStatus es un enum que contiene strings
    @Enumerated(EnumType.STRING) //Mapea a la bd como string
    private ToolStatus initialState;

    @Enumerated(EnumType.STRING)
    private ToolStatus currentState; //Gestion de estado

}
