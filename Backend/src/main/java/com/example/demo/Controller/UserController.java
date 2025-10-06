package com.example.demo.Controller;

import com.example.demo.DTOs.AuthResponseDTO;
import com.example.demo.DTOs.UserLoginDTO;
import com.example.demo.DTOs.UserRegisterDTO;
import com.example.demo.DTOs.UserResponseDTO;
import com.example.demo.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUser() {
        List<UserResponseDTO> userList = userService.getAllUsersDTO();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegisterDTO user) {
        UserResponseDTO createdUser = userService.createUsers(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUsers(@Valid @RequestBody UserLoginDTO dto) {
        AuthResponseDTO loginUser = userService.loginUsers(dto.getUserRut(), dto.getUserPassword());
        return ResponseEntity.ok(loginUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUsersById(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (IllegalArgumentException errorDeleteUserById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteUserById.getMessage());
        }
    }
}
