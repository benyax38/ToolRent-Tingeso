package com.example.demo.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({
        "userId",
        "userFirstName",
        "userLastName",
        "userRut",
        "userPhone",
        "userEmail",
        "userPassword",
        "roleId"
})

// Enviado por el frontend para crear un nuevo usuario
public class UserRegisterDTO {

    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userRut;
    private String userPhone;
    private String userEmail;
    private String userPassword;

    private Long roleId;
}