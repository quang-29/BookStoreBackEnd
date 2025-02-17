package org.example.bookstore.configuration;

import org.example.bookstore.model.Role;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@Configuration
public class ApplicationInitConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));

            Role userRole = roleRepository.findByRoleName("USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "USER")));
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(new HashSet<>(Collections.singleton(adminRole)))
                        .build();
                userRepository.save(admin);
                log.info("Admin account created: username='admin', password='admin'. Please change it!");
            }
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user"))
                        .roles(new HashSet<>(Collections.singleton(userRole)))
                        .build();
                userRepository.save(user);
                log.info("User account created: username='user', password='user'.");
            }
        };
    }
}
