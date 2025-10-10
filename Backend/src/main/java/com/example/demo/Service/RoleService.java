package com.example.demo.Service;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.Entity.RoleEntity;
import com.example.demo.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) { this.roleRepository = roleRepository; }

    // Aqui se definen los roles del sistema
    public enum UserRoles {
        ADMIN,
        EMPLOYEE
    }

    public List<RoleDTO> getAllRolesDTO() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleDTO(
                        role.getRoleId(),
                        role.getRoleName().name()
                ))
                .toList();
    }

    public RoleEntity createRoles(RoleEntity role) {
        return roleRepository.save(role);
    }

    public void deleteRolesById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe rol con id: " + id);
        }
        roleRepository.deleteById(id);
    }
}
