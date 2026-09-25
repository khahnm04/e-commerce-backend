package com.khahnm18.ecommercebackend.service.storage.impl;

import com.khahnm18.ecommercebackend.config.properties.CloudflareR2Properties;
import com.khahnm18.ecommercebackend.dto.response.storage.PresignedResponse;
import com.khahnm18.ecommercebackend.service.storage.StorageService;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.util.UUID;

@Slf4j(topic = "R2STORAGE-SERVICE")
@Service
@RequiredArgsConstructor
public class R2StorageServiceImpl implements StorageService {

    private final S3Presigner s3Presigner;
    private final CloudflareR2Properties cloudflareR2Properties;

    @Override
    public PresignedResponse generatePresignedUrl(String folder, String fileName) {
        String key = generateKey(folder, fileName);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(cloudflareR2Properties.getBucket())
            .key(key)
            .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(objectRequest)
            .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String url = presignedRequest.url().toExternalForm();
        return PresignedResponse.builder()
            .url(url)
            .key(key)
            .build();
    }

    private String generateKey(String folder, String originalFilename) {
        return folder + UUID.randomUUID() + "-" + originalFilename;
    }

}
