package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

// Devuelve al frontend los datos del usuario sin exponer la contraseña
public class UserResponseDTO {

    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userRut;
    private String userPhone;
    private String userEmail;
    private RoleDTO role;
}

