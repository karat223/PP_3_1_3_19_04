package ru.kata.spring.boot_security.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.model.Role;
import ru.kata.spring.boot_security.model.User;
import ru.kata.spring.boot_security.repository.RoleRepository;
import ru.kata.spring.boot_security.repository.UserRepository;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            // Create roles
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // Create admin user
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setAge(30);
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);

            // Create regular user
            User user = new User();
            user.setFirstName("User");
            user.setLastName("User");
            user.setAge(25);
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }
}
