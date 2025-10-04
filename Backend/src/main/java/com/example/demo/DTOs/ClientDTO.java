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

public class ClientDTO {

    private Long ClientId;
    private String ClientFirstName;
    private String ClientLastName;
    private String ClientRut;
    private String ClientPhone;
    private String ClientEmail;
}
