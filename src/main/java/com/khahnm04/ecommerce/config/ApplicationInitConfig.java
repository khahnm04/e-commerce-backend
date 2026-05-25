package com.khahnm04.ecommerce.config;

import com.khahnm04.ecommerce.entity.auth.EmployeeRole;
import com.khahnm04.ecommerce.entity.auth.Permission;
import com.khahnm04.ecommerce.entity.auth.Role;
import com.khahnm04.ecommerce.entity.auth.RolePermission;
import com.khahnm04.ecommerce.entity.user.Employee;
import com.khahnm04.ecommerce.constant.user.EmployeeStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import com.khahnm04.ecommerce.repository.auth.EmployeeRoleRepository;
import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
import com.khahnm04.ecommerce.repository.auth.RolePermissionRepository;
import com.khahnm04.ecommerce.repository.auth.RoleRepository;
import com.khahnm04.ecommerce.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j(topic = "APPLICATION-INIT")
@Component
@RequiredArgsConstructor
public class ApplicationInitConfig implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        /*
         * =========================
         * 1. CREATE ADMIN ROLE
         * =========================
         */
        Role existingRole = roleRepository.findByCodeAndIsDeletedFalse("ADMIN")
            .orElse(null);

        if (existingRole != null) {
            log.info("ADMIN role already exists. Skip initialization.");
            return;
        }

        Role adminRole = roleRepository.save(
            Role.builder()
                .code("ADMIN")
                .name("Quản trị viên")
                .description("Quản trị toàn hệ thống")
                .build()
        );

        /*
         * =========================
         * 2. CREATE PERMISSIONS
         * =========================
         */

        List<Permission> defaultPermissions = List.of(
            Permission.builder()
                .code("ROLE_VIEW")
                .name("View Role")
                .module("ROLE")
                .description("Xem danh sách role")
                .build(),

            Permission.builder()
                .code("ROLE_CREATE")
                .name("Create Role")
                .module("ROLE")
                .description("Tạo role mới")
                .build(),

            Permission.builder()
                .code("ROLE_UPDATE")
                .name("Update Role")
                .module("ROLE")
                .description("Cập nhật role")
                .build(),

            Permission.builder()
                .code("ROLE_DELETE")
                .name("Delete Role")
                .module("ROLE")
                .description("Xóa role")
                .build()
        );

        for (Permission p : defaultPermissions) {
            Permission permission = permissionRepository
                .findByCodeAndDeletedAtIsNull(p.getCode())
                .orElseGet(() -> permissionRepository.save(p));

            boolean exists = rolePermissionRepository
                .existsByRoleIdAndPermissionId(adminRole.getId(), permission.getId());

            if (!exists) {
                rolePermissionRepository.save(
                    RolePermission.builder()
                        .role(adminRole)
                        .permission(permission)
                        .build()
                );
            }
        }

        /*
         * =========================
         * 3. CREATE ADMIN EMPLOYEE
         * =========================
         */

        Employee admin = employeeRepository.findByEmailAndDeletedAtIsNull("admin@gmail.com")
            .orElseGet(() -> employeeRepository.save(
                Employee.builder()
                    .code("EMP-ADMIN-001")
                    .fullName("Nguyễn Minh Khánh")
                    .jobTitle("Quản trị viên")
                    .email("admin@gmail.com")
                    .phoneNumber("0387011685")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .gender(Gender.OTHER)
                    .status(EmployeeStatus.ACTIVE)
                    .build()
            ));

        /*
         * =========================
         * 4. ASSIGN ROLE TO EMPLOYEE
         * =========================
         */

        boolean existsEmployeeRole = employeeRoleRepository
            .existsByEmployeeIdAndRoleId(admin.getId(), adminRole.getId());

        if (!existsEmployeeRole) {
            employeeRoleRepository.save(
                EmployeeRole.builder()
                    .employee(admin)
                    .role(adminRole)
                    .build()
            );
        }

        log.info("Application initialized successfully");
    }

}
