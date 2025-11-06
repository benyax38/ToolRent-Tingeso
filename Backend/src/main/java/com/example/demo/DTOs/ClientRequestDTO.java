package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/*
    * El objetivo de este DTO es crear la plantilla para el JSON
    * que se requiere para la creacion de un cliente
 */
public class ClientRequestDTO {
    private String clientFirstName;
    private String clientLastName;
    private String clientRut;
    private String clientPhone;
    private String clientEmail;
}
