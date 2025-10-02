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
        "role"
})

public class UserDTO {

    private Long UserId;
    private String UserFirstName;
    private String UserLastName;
    private String UserRut;
    private String UserPhone;
    private String UserEmail;

    private RoleDTO Role;
}
