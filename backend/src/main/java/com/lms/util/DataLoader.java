package com.lms.util;

import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
// The RoleRepository is no longer needed/imported

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!test")
public class DataLoader {

    // Dependency on RoleRepository is removed from the method signature
    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder // Only UserRepository and PasswordEncoder remain
    ) {
        return args -> {
            // Check if user 'admin' already exists
            if (userRepository.findByUsername("admin").isEmpty()) {

                System.out.println("Seeding initial admin user: 'admin'");

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@lms.com");
                admin.setFirstName("System");
                admin.setLastName("Admin");

                // CRITICAL FIX: Set the Role directly using the Enum constant
                admin.setRole(Role.ADMIN);
                admin.setIsApproved(true);

                admin.setPassword(passwordEncoder.encode("admin123"));

                userRepository.save(admin);
                System.out.println("User 'admin' created successfully.");
            }

            // Example of seeding an instructor user
            if (userRepository.findByUsername("instructor1").isEmpty()) {

                System.out.println("Seeding initial instructor user: 'instructor1'");

                User instructor = new User();
                instructor.setUsername("instructor1");
                instructor.setEmail("instructor@lms.com");
                instructor.setFirstName("John");
                instructor.setLastName("Doe");
                instructor.setRole(Role.INSTRUCTOR);
                instructor.setIsApproved(true);
                instructor.setPassword(passwordEncoder.encode("instructor123"));

                userRepository.save(instructor);
                System.out.println("User 'instructor1' created successfully.");
            }

             if (userRepository.findByUsername("student1").isEmpty()) {

                System.out.println("Seeding initial instructor user: 'student1'");

                User student = new User();
                student.setUsername("student1");
                student.setEmail("student@lms.com");
                student.setFirstName("Student");
                student.setLastName("s");
                student.setRole(Role.STUDENT);
                student.setIsApproved(true);
                student.setPassword(passwordEncoder.encode("student123"));

                userRepository.save(student);
                System.out.println("User 'student123' created successfully.");
            }
        };
    }
}
