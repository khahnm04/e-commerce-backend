package com.khahnm18.ecommercebackend;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class ECommerceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceBackendApplication.class, args);
    }

}
