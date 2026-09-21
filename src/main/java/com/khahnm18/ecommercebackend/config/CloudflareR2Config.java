package com.khahnm18.ecommercebackend.config;

import com.khahnm18.ecommercebackend.config.properties.CloudflareR2Properties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import lombok.RequiredArgsConstructor;
import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class CloudflareR2Config {

    private final CloudflareR2Properties cloudflareR2Properties;

    private StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                cloudflareR2Properties.getAccessKey(),
                cloudflareR2Properties.getSecretKey()
            )
        );
    }

    private S3Configuration s3Configuration() {
        return S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .chunkedEncodingEnabled(false)
            .build();
    }

    @Bean
    S3Client s3Client() {
        return S3Client.builder()
            .endpointOverride(URI.create(cloudflareR2Properties.getEndpoint()))
            .region(Region.of("auto"))
            .credentialsProvider(credentialsProvider())
            .serviceConfiguration(s3Configuration())
            .build();
    }

    @Bean
    S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .endpointOverride(URI.create(cloudflareR2Properties.getEndpoint()))
            .region(Region.of("auto"))
            .credentialsProvider(credentialsProvider())
            .serviceConfiguration(s3Configuration())
            .build();
    }

}
