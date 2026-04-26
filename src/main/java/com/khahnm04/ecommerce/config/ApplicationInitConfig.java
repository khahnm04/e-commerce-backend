package com.khahnm04.ecommerce.config;

import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.entity.Role;
import com.khahnm04.ecommerce.repository.EmployeeRepository;
import com.khahnm04.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        return app -> {
            Role adminRole = roleRepository.findByCode("ADMIN").orElseGet(() -> {
                Role role = Role.builder()
                        .code("ADMIN")
                        .name("Administrator")
                        .description("Full system access")
                        .build();
                roleRepository.save(role);
                log.warn("ADMIN role has been created");
                return role;
            });

            if (employeeRepository.findByPhoneNumber("0812345678").isEmpty()) {
                Employee employee = Employee.builder()
                        .phoneNumber("0812345678")
                        .email("admin@gmail.com")
                        .fullName("System Admin")
                        .code("ADMIN000")
                        .password(passwordEncoder.encode("Admin@1234"))
                        .roles(Set.of(adminRole))
                        .build();

                employeeRepository.save(employee);
                log.warn("Admin employee has been created with default password: Admin@1234, please change it");
            }
        };
    }

}
