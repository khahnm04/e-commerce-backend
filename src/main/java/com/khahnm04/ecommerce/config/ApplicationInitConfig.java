package com.khahnm04.ecommerce.config;

import com.khahnm04.ecommerce.entity.auth.EmployeeRole;
import com.khahnm04.ecommerce.entity.user.Employee;
import com.khahnm04.ecommerce.constant.user.EmployeeStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import com.khahnm04.ecommerce.repository.auth.EmployeeRoleRepository;
import com.khahnm04.ecommerce.repository.auth.RoleRepository;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "APPLICATION-INIT")
@Component
@RequiredArgsConstructor
public class ApplicationInitConfig implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting database initialization for STAFF employee...");

        roleRepository.findByCodeAndIsDeletedFalse("STAFF").ifPresentOrElse(
            staffRole -> {
                // 1. Tìm hoặc tạo mới nhân viên Nguyễn Văn Nam
                Employee staff = employeeRepository.findByEmailAndDeletedAtIsNull("namnv@gmail.com")
                    .orElseGet(() -> {
                        log.info("Creating new employee: Nguyen Van Nam (namnv@gmail.com)");
                        return employeeRepository.save(
                            Employee.builder()
                                .code("EMP-STAFF-001")
                                .fullName("Nguyễn Văn Nam")
                                .jobTitle("Nhân viên")
                                .email("namnv@gmail.com")
                                .phoneNumber("0987654321")
                                .password(passwordEncoder.encode("Staff@1234"))
                                .gender(Gender.MALE)
                                .status(EmployeeStatus.ACTIVE)
                                .build()
                        );
                    });

                // 2. Gắn role STAFF cho nhân viên nếu chưa có
                boolean existsStaffRole = employeeRoleRepository
                    .existsByEmployeeIdAndRoleId(staff.getId(), staffRole.getId());

                if (!existsStaffRole) {
                    employeeRoleRepository.save(
                        EmployeeRole.builder()
                            .employee(staff)
                            .role(staffRole)
                            .build()
                    );
                    log.info("Successfully assigned STAFF role to Nguyen Van Nam (namnv@gmail.com)");
                } else {
                    log.info("Employee Nguyen Van Nam already has STAFF role. Skipping mapping.");
                }
            },
            () -> log.warn("Role 'STAFF' does not exist in the database. Cannot initialize Nguyen Van Nam.")
        );

        log.info("Database initialization completed.");
    }

}
