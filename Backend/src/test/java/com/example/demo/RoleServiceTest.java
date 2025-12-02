package com.example.demo;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.Entity.RoleEntity;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    // ---------------------------------------------------------
    // 🔍 getAllRolesDTO()
    // ---------------------------------------------------------
    @Test
    void testGetAllRolesDTO_ReturnsList() {
        // Datos simulados
        RoleEntity r1 = new RoleEntity();
        r1.setRoleId(1L);
        r1.setRoleName(RoleService.UserRoles.ADMIN);

        RoleEntity r2 = new RoleEntity();
        r2.setRoleId(2L);
        r2.setRoleName(RoleService.UserRoles.EMPLOYEE);

        when(roleRepository.findAll()).thenReturn(List.of(r1, r2));

        // Ejecución
        List<RoleDTO> result = roleService.getAllRolesDTO();

        // Verificación
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getRoleId());
        assertEquals("ADMIN", result.get(0).getRoleName());
        assertEquals(2L, result.get(1).getRoleId());
        assertEquals("EMPLOYEE", result.get(1).getRoleName());

        verify(roleRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // 🟩 createRoles()
    // ---------------------------------------------------------
    @Test
    void testCreateRoles_SavesRole() {
        RoleEntity role = new RoleEntity();
        role.setRoleId(1L);
        role.setRoleName(RoleService.UserRoles.ADMIN);

        when(roleRepository.save(role)).thenReturn(role);

        RoleEntity result = roleService.createRoles(role);

        assertNotNull(result);
        assertEquals(1L, result.getRoleId());
        assertEquals(RoleService.UserRoles.ADMIN, result.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    // ---------------------------------------------------------
    // ❌ deleteRolesById() - role exists
    // ---------------------------------------------------------
    @Test
    void testDeleteRolesById_ExistingRole() {
        Long roleId = 1L;
        when(roleRepository.existsById(roleId)).thenReturn(true);

        roleService.deleteRolesById(roleId);

        verify(roleRepository, times(1)).existsById(roleId);
        verify(roleRepository, times(1)).deleteById(roleId);
    }

    // ---------------------------------------------------------
    // ⚠ deleteRolesById() - role missing
    // ---------------------------------------------------------
    @Test
    void testDeleteRolesById_NonExistingRole_Throws() {
        Long roleId = 99L;

        when(roleRepository.existsById(roleId)).thenReturn(false);

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.deleteRolesById(roleId)
        );

        assertEquals("No existe rol con id: 99", ex.getMessage());

        verify(roleRepository, times(1)).existsById(roleId);
        verify(roleRepository, never()).deleteById(any());
    }
}
