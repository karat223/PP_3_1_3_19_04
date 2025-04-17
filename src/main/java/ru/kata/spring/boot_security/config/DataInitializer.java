package ru.kata.spring.boot_security.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.model.Role;
import ru.kata.spring.boot_security.model.User;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if roles already exist
        Long rolesCount = entityManager.createQuery("SELECT COUNT(r) FROM Role r", Long.class)
                .getSingleResult();

        if (rolesCount == 0) {
            // Create roles
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            entityManager.persist(adminRole);
            entityManager.persist(userRole);

            // Create admin user
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setAge(30);
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            admin.setRoles(adminRoles);
            entityManager.persist(admin);

            // Create regular user
            User user = new User();
            user.setFirstName("User");
            user.setLastName("User");
            user.setAge(25);
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            user.setRoles(userRoles);
            entityManager.persist(user);
        }
    }
}