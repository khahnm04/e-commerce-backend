package com.khahnm18.ecommercebackend.config.init;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.constant.user.GenderType;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.AccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import com.khahnm18.ecommercebackend.repository.auth.AccountRepository;
import com.khahnm18.ecommercebackend.repository.auth.AccountRoleRepository;
import com.khahnm18.ecommercebackend.repository.auth.RoleRepository;
import com.khahnm18.ecommercebackend.repository.user.AdminProfileRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Slf4j(topic = "APPLICATIION-INIT")
@Configuration
@RequiredArgsConstructor
public class ApplicationInit {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
        AccountRepository accountRepository,
        RoleRepository roleRepository,
        AccountRoleRepository accountRoleRepository,
        AdminProfileRepository adminProfileRepository
    ) {

        return args -> {
            log.info("Initializing application data...");

            String email = "admin@gmail.com";
            String password = "Admin@1234";

            if (accountRepository.findByEmailAndIsDeletedFalse(email).isEmpty()) {

                // 1. Tạo hoặc lấy Role ADMIN
                Role adminRole = roleRepository.findAll().stream()
                    .filter(r -> "ADMIN".equalsIgnoreCase(r.getCode()))
                    .findFirst()
                    .orElseGet(() -> roleRepository.save(Role.builder()
                        .code("ADMIN")
                        .name("Administrator")
                        .build()));

                // 2. Tạo tài khoản Admin mặc định
                Account adminAccount = Account.builder()
                    .email(email)
                    .phoneNumber("0276000574")
                    .password(passwordEncoder.encode(password))
                    .status(AccountStatus.ACTIVE)
                    .build();
                adminAccount = accountRepository.save(adminAccount);

                // 3. Gán Role ADMIN cho tài khoản Admin
                accountRoleRepository.save(AccountRole.builder()
                    .accountId(adminAccount.getId())
                    .roleId(adminRole.getId())
                    .build());

                // 4. Tạo AdminProfile
                AdminProfile adminProfile = AdminProfile.builder()
                    .accountId(adminAccount.getId())
                    .fullName("System Administrator")
                    .jobTitle("Super Admin")
                    .gender(GenderType.OTHER)
                    .dateOfBirth(LocalDate.of(2000, 1, 1))
                    .build();
                adminProfileRepository.save(adminProfile);

                log.warn("admin user has been created with email: {} password: {}, please change it", email, password);
            }
        };
    }

}
