package com.example.demo;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTOs.*;
import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Exception.DuplicateUserException;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.RoleService;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    // ───────────────────────────────────────────────────────────────
    // TEST: getAllUsersDTO
    // ───────────────────────────────────────────────────────────────
    @Test
    void testGetAllUsersDTO() {
        RoleEntity role = new RoleEntity();
        role.setRoleId(1L);
        role.setRoleName(RoleService.UserRoles.ADMIN);

        UserEntity user = new UserEntity();
        user.setUserId(20L);
        user.setUserFirstName("Anna");
        user.setUserLastName("Lopez");
        user.setUserRut("12.345.678-9");
        user.setUserPhone("555555");
        user.setUserEmail("anna@mail.com");
        user.setRoles(role);

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.getAllUsersDTO();

        assertEquals(1, result.size());
        assertEquals("Anna", result.get(0).getUserFirstName());
        assertEquals("ADMIN", result.get(0).getRole().getRoleName());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: createUsers (caso OK)
    // ───────────────────────────────────────────────────────────────
    @Test
    void testCreateUsers_success() {

        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserFirstName("Ben");
        dto.setUserLastName("Sandoval");
        dto.setUserRut("1-9");
        dto.setUserPhone("123");
        dto.setUserEmail("b@b.com");
        dto.setUserPassword("abc");
        dto.setRoleId(1L);

        // Rol encontrado
        RoleEntity role = new RoleEntity();
        role.setRoleId(1L);
        role.setRoleName(RoleService.UserRoles.EMPLOYEE);

        when(userRepository.findByUserRut("1-9")).thenReturn(Optional.empty());
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        UserResponseDTO result = userService.createUsers(dto);

        assertEquals("Ben", result.getUserFirstName());
        assertEquals("EMPLOYEE", result.getRole().getRoleName());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: createUsers lanza DuplicateUserException
    // ───────────────────────────────────────────────────────────────
    @Test
    void testCreateUsers_duplicateRut() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserRut("1-9");

        when(userRepository.findByUserRut("1-9"))
                .thenReturn(Optional.of(new UserEntity()));

        assertThrows(DuplicateUserException.class,
                () -> userService.createUsers(dto));
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: createUsers lanza RuntimeException (Rol no encontrado)
    // --- NUEVO PARA COBERTURA 100% ---
    // ───────────────────────────────────────────────────────────────
    @Test
    void testCreateUsers_roleNotFound() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserRut("2-8");
        dto.setRoleId(99L);
        dto.setUserPassword("pass");

        when(userRepository.findByUserRut("2-8")).thenReturn(Optional.empty());
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class,
                () -> userService.createUsers(dto));

        assertEquals("Rol no encontrado", ex.getMessage());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: loginUsers (caso OK)
    // ───────────────────────────────────────────────────────────────
    @Test
    void testLoginUsers_success() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPw = encoder.encode("1234");

        RoleEntity role = new RoleEntity();
        role.setRoleId(1L);
        role.setRoleName(RoleService.UserRoles.ADMIN);

        UserEntity user = new UserEntity();
        user.setUserRut("12");
        user.setUserPassword(encodedPw);
        user.setRoles(role);

        when(userRepository.findByUserRut("12")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("12", "ADMIN")).thenReturn("fake-token");

        AuthResponseDTO response = userService.loginUsers("12", "1234");

        assertEquals("fake-token", response.getToken());
        assertEquals("12", response.getUser().getUserRut());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: loginUsers usuario no encontrado
    // ───────────────────────────────────────────────────────────────
    @Test
    void testLoginUsers_userNotFound() {

        when(userRepository.findByUserRut("12")).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class,
                () -> userService.loginUsers("12", "1234"));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: loginUsers credenciales incorrectas
    // --- NUEVO PARA COBERTURA 100% ---
    // ───────────────────────────────────────────────────────────────
    @Test
    void testLoginUsers_wrongPassword() {
        // Generamos un hash real para la password "correcta"
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPw = encoder.encode("correctPassword");

        UserEntity user = new UserEntity();
        user.setUserRut("12");
        user.setUserPassword(encodedPw); // En BD está la correcta

        when(userRepository.findByUserRut("12")).thenReturn(Optional.of(user));

        // Intentamos login con "wrongPassword"
        // Como el servicio usa 'new BCryptPasswordEncoder()' internamente, funcionará correctamente validando el hash
        Exception ex = assertThrows(RuntimeException.class,
                () -> userService.loginUsers("12", "wrongPassword"));

        assertEquals("Credenciales incorrectas", ex.getMessage());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: mapToUserResponseDTO (Directo)
    // --- OPCIONAL PERO BUENO PARA ASEGURAR COBERTURA ---
    // ───────────────────────────────────────────────────────────────
    @Test
    void testMapToUserResponseDTO() {
        RoleEntity role = new RoleEntity();
        role.setRoleId(5L);
        role.setRoleName(RoleService.UserRoles.EMPLOYEE);

        UserEntity user = new UserEntity();
        user.setUserId(100L);
        user.setUserFirstName("Test");
        user.setUserLastName("User");
        user.setUserEmail("test@test.com");
        user.setUserRut("99-9");
        user.setUserPhone("999");
        user.setRoles(role);

        UserResponseDTO dto = userService.mapToUserResponseDTO(user);

        assertEquals(100L, dto.getUserId());
        assertEquals("Test", dto.getUserFirstName());
        assertEquals("EMPLOYEE", dto.getRole().getRoleName());
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: deleteUsersById OK
    // ───────────────────────────────────────────────────────────────
    @Test
    void testDeleteUsersById_success() {
        when(userRepository.existsById(10L)).thenReturn(true);

        userService.deleteUsersById(10L);

        verify(userRepository, times(1)).deleteById(10L);
    }

    // ───────────────────────────────────────────────────────────────
    // TEST: deleteUsersById lanza error
    // ───────────────────────────────────────────────────────────────
    @Test
    void testDeleteUsersById_notFound() {
        when(userRepository.existsById(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUsersById(10L));

        assertTrue(ex.getMessage().contains("No existe usuario"));
    }
}