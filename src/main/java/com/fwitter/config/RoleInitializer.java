package com.fwitter.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fwitter.models.Role;
import com.fwitter.repositories.RoleRepository;


@Component
public class RoleInitializer {

    @Bean
    public CommandLineRunner initializeRoles(RoleRepository roleRepo) {
        return args -> {
            if (roleRepo.findByAuthority("USER").isEmpty()) {
                Role userRole = new Role();
                userRole.setAuthority("USER");
                roleRepo.save(userRole);
            }
            if (roleRepo.findByAuthority("ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setAuthority("ADMIN");
                roleRepo.save(adminRole);
            }
        };
    }
}
