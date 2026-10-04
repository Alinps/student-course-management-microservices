package com.example.auth_service.config;

import com.example.auth_service.enums.RoleName;
import com.example.auth_service.models.Role;
import com.example.auth_service.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    CommandLineRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {

            for (RoleName roleName : RoleName.values()) {

                if (!roleRepository.existsByName(roleName)) {
                    Role role  = Role.builder()
                            .name(roleName)
                            .build();
                    roleRepository.save(role);
                }
            }
        };
    }
}
