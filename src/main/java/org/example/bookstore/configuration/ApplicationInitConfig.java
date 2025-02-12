package org.example.bookstore.configuration;

import org.example.bookstore.model.Role;


import org.example.bookstore.model.User;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class ApplicationInitConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner (UserRepository userRepository, RoleRepository roleRepository){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                Role adminRole = new Role();
                adminRole.setRoleName("ADMIN");
                roleRepository.save(adminRole);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(Collections.singleton(adminRole))
                        .build();
                userRepository.save(user);


                log.info("Admin has been created with name admin and password admin. Please change it!");
            }
        };
    }
}
