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
@Table(name = "tools")

//Lombok
@Data //Genera automaticamente getters, setters y metodos extra
@NoArgsConstructor //Genera un constructor vacio
@AllArgsConstructor //Genera un constructor con todos los atributos
@Builder //Permite ingresar los argumentos del constructor en cualquier orden

public class ToolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tool_id")
    private Long toolId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_tool_state", length = 20)
    private ToolService.ToolStatus currentToolState; //Gestion de estado

    @OneToOne(mappedBy = "tools")
    private LoanEntity loan;

    @OneToMany(mappedBy = "tools", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KardexEntity> kardexes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tool_catalog_id", nullable = false)
    private ToolCatalogEntity tool_catalogs;
}
