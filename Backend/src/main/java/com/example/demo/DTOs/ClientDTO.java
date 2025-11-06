package com.example.demo.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({
        "clientId",
        "clientFirstName",
        "clientLastName",
        "clientRut",
        "clientPhone",
        "clientEmail"
})

/*
    * El objetivo de este DTO es mostrar la informacion de los clientes
    * en el endpoint que hace get all
 */
public class ClientDTO {

    private Long ClientId;
    private String ClientFirstName;
    private String ClientLastName;
    private String ClientRut;
    private String ClientPhone;
    private String ClientEmail;
    private String ClientStatus;
}
