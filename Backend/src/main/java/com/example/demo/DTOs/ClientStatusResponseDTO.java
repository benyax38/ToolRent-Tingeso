package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/*
    * El objetivo de este DTO es mostrar informacion clave del cliente una vez
    * que se activa el endpoint updateClientState de tipo PATCH que actualiza el clientState
 */
public class ClientStatusResponseDTO {
    private Long clientId;
    private String clientName;
    private String clientState;
}
