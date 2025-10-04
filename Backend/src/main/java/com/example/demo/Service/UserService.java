package com.example.demo.Service;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.DTOs.UserLoginDTO;
import com.example.demo.DTOs.UserRegisterDTO;
import com.example.demo.DTOs.UserResponseDTO;
import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Exception.DuplicateUserException;
import com.example.demo.Exception.InvalidCredentialsException;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserResponseDTO> getAllUsersDTO() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDTO userResponseDTO = new UserResponseDTO();
                    userResponseDTO.setUserId(user.getUserId());
                    userResponseDTO.setUserFirstName(user.getUserFirstName());
                    userResponseDTO.setUserLastName(user.getUserLastName());
                    userResponseDTO.setUserRut(user.getUserRut());
                    userResponseDTO.setUserPhone(user.getUserPhone());
                    userResponseDTO.setUserEmail(user.getUserEmail());
                    userResponseDTO.setRole(new RoleDTO(user.getRoles().getRoleId(), user.getRoles().getRoleName()));
                    return userResponseDTO;
                })
                .toList();
    }

    public UserResponseDTO createUsers(UserRegisterDTO dto) {
        // Verificar que no exista el usuario por RUT
        if (userRepository.findByUserRut(dto.getUserRut()).isPresent()) {
            throw new DuplicateUserException("El RUT ya está registrado");
        }

        // Crear la entidad Usuario
        UserEntity user = new UserEntity();
        user.setUserFirstName(dto.getUserFirstName());
        user.setUserLastName(dto.getUserLastName());
        user.setUserRut(dto.getUserRut());
        user.setUserPhone(dto.getUserPhone());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));

        // Asignar rol
        RoleEntity role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRoles(role);

        // Guardar en DB
        UserEntity savedUser = userRepository.save(user);

        // Convertir a DTO de salida
        RoleDTO roleDTO = new RoleDTO(role.getRoleId(), role.getRoleName());
        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUserFirstName(),
                savedUser.getUserLastName(),
                savedUser.getUserRut(),
                savedUser.getUserPhone(),
                savedUser.getUserEmail(),
                roleDTO
        );
    }

    public UserResponseDTO loginUsers(UserLoginDTO dto) {
        // Obtener usuario como Optional
        Optional<UserEntity> optionalUser = userRepository.findByUserRut(dto.getUserRut());

        // Validar existencia
        UserEntity user = optionalUser.orElseThrow(
                () -> new InvalidCredentialsException("RUT o contraseña incorrectos")
        );

        // Validar contraseña
        if (!passwordEncoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            throw new InvalidCredentialsException("RUT o contraseña incorrectos");
        }

        // Convertir a DTO de salida
        RoleDTO roleDTO = new RoleDTO(user.getRoles().getRoleId(), user.getRoles().getRoleName());
        return new UserResponseDTO(
                user.getUserId(),
                user.getUserFirstName(),
                user.getUserLastName(),
                user.getUserRut(),
                user.getUserPhone(),
                user.getUserEmail(),
                roleDTO
        );
    }


    public void deleteUsersById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe usuario con id: " + id);
        }
        userRepository.deleteById(id);
    }
}
