package com.example.demo.Controller;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.Entity.RoleEntity;
import com.example.demo.Service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService) { this.roleService = roleService; }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRole() {
        List<RoleDTO> roleList = roleService.getAllRolesDTO();
        return ResponseEntity.ok(roleList);
    }

    @PostMapping
    public ResponseEntity<RoleEntity> createRole(@Valid @RequestBody RoleEntity role) {
        RoleEntity createdRole = roleService.createRoles(role);
        return ResponseEntity.ok(createdRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoleById(@PathVariable Long id) {
        try {
            roleService.deleteRolesById(id);
            return ResponseEntity.ok("Rol eliminado correctamente");
        } catch (IllegalArgumentException errorDeleteRoleById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteRoleById.getMessage());
        }
    }
}
