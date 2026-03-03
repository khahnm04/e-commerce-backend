package com.khahnm04.ecommerce.common.config;

import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.repository.UserRepository;
import com.khahnm04.ecommerce.modules.auth.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return app -> {
            if (userRepository.findByPhoneNumber("0812345678").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(UserRole.SUPER_ADMIN.name());

                User user = User.builder()
                        .phoneNumber("0812345678")
                        .email("admin@gmail.com")
                        .fullName("System Admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }

}
