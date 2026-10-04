package com.example.auth_service.config;

import com.example.auth_service.enums.RoleName;
import com.example.auth_service.exception.ResourceNotFoundException;
import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import com.example.auth_service.repository.RoleRepository;
import com.example.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Order(2)
@RequiredArgsConstructor
public class InitialAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial-admin.username}")
    private String adminUsername;

    @Value("${app.initial-admin.email}")
    private String adminEmail;

    @Value("${app.initial-admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args)  {

        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }
        Role adminRole = roleRepository
                .findByName(RoleName.ADMIN)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ADMIN role not found"));

        User admin =  User.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .enabled(true)
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(admin);
    }


}
