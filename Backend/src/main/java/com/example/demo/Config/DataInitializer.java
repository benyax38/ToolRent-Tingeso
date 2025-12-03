package com.example.demo.Config;

import com.example.demo.Entity.RoleEntity;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        // Solo crear roles si no existen
        if(roleRepository.count()==0){
            roleRepository.save(new RoleEntity(null, RoleService.UserRoles.ADMIN, new ArrayList<>()));
            roleRepository.save(new RoleEntity(null, RoleService.UserRoles.EMPLOYEE, new ArrayList<>()));
            System.out.println("Roles iniciales creados");
        }
    }
}
