package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration.security;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.UserRole;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario Doctor
        if (!userRepository.existsByUsername("doctor")) {
            String encryptedPassword = new BCryptPasswordEncoder().encode("doctor");
            User doctorUser = new User("Doctor", "User", "doctor", encryptedPassword, UserRole.DOCTOR, Set.of("READ_PRIVILEGES", "WRITE_PRIVILEGES"));
            userRepository.save(doctorUser);
            System.out.println("Doctor user created!");
        }

        // Crear usuario Manager
        if (!userRepository.existsByUsername("manager")) {
            String encryptedPassword = new BCryptPasswordEncoder().encode("manager");
            User managerUser = new User("Manager", "User", "manager", encryptedPassword, UserRole.MANAGER, Set.of("READ_PRIVILEGES", "WRITE_PRIVILEGES"));
            userRepository.save(managerUser);
            System.out.println("Manager user created!");
        }

        // Crear usuario Admin
        if (!userRepository.existsByUsername("admin")) {
            String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
            User adminUser = new User("Admin", "User", "admin", encryptedPassword, UserRole.ADMIN, Set.of("READ_PRIVILEGES", "WRITE_PRIVILEGES", "ADMIN_PRIVILEGES"));
            userRepository.save(adminUser);
            System.out.println("Admin user created!");
        }
    }
}