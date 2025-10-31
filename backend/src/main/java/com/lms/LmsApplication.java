package com.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder; // Assuming you use this for the password
import com.lms.repository.UserRepository; // Assuming your repository path
import com.lms.model.User; // Assuming your User entity path
import com.lms.model.Role; // Assuming your Role enum/class path

/**
 * The entry point for the Spring Boot application (Learning Management System).
 * The @SpringBootApplication annotation is a convenience annotation that adds:
 * - @Configuration: Tags the class as a source of bean definitions.
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings.
 * - @ComponentScan: Tells Spring to look for other components, configurations, and services in the 'com.lms' package.
 */
@SpringBootApplication
@EnableJpaAuditing
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

    // Optional: Use CommandLineRunner to execute code immediately after startup.
    // This is often used for creating an initial admin user or essential roles.
    @Bean
    public CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the initial admin user exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                System.out.println("Creating default admin user...");

                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setUsername("admin");
                admin.setEmail("admin@lms.com");

                // IMPORTANT: Use the PasswordEncoder for security
                admin.setPassword(passwordEncoder.encode("admin123")); // Use a secure default password

                // Assuming you have a Role enum or class
                admin.setRole(Role.ADMIN);
                admin.setIsApproved(true);

                userRepository.save(admin);
                System.out.println("Default admin user created with username: 'admin'");
            }
        };
    }
}
