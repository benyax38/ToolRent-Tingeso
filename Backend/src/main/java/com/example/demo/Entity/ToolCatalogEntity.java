package com.example.demo.Entity;

import com.example.demo.Service.ToolService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//JPA/Hibernate
@Entity //Mapea a una tabla en la base de datos
@Table(name = "tool_catalogs")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class ToolCatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tool_catalog_id")
    private Long toolCatalogId; //El tipo de dato Long permite que hibernate distinga entre 0 y null

    @Column(name = "tool_name", length = 30)
    private String toolName;

    @Column(name = "tool_category", length = 20)
    private String toolCategory;

    //ToolStatus es un enum que contiene strings
    @Enumerated(EnumType.STRING) //Mapea a la bd como string
    @Column(name = "initial_tool_state", length = 20)
    private ToolService.ToolStatus initialToolState;

    @Column(name = "rental_value")
    private Double rentalValue;

    @Column(name = "replacement_value")
    private Double replacementValue;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "available_units")
    private int availableUnits; //Manejo de STOCK

    @OneToMany(mappedBy = "tool_catalogs", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToolEntity> tools = new ArrayList<>();

    @OneToMany(mappedBy = "tool_catalogs", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KardexEntity> kardexes = new ArrayList<>();
}
