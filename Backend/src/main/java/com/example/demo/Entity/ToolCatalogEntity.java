package com.example.demo.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "El nombre de la herramienta es obligatorio")
    @Column(name = "tool_name", length = 30)
    private String toolName;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(name = "tool_category", length = 20)
    private String toolCategory;

    @Column(name = "daily_rental_value")
    private Double dailyRentalValue;

    @NotNull(message = "El valor de reposición es obligatorio")
    @Positive(message = "El valor de reposición debe ser mayor que 0")
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
