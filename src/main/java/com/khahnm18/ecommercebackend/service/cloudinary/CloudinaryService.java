package com.khahnm18.ecommercebackend.service.cloudinary;

import com.khahnm18.ecommercebackend.dto.response.cloudinary.SignatureResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j(topic = "CLOUDINARY-SERVICE")
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    private static final String UPLOAD_FOLDER = "ecommerce-backend";

    @SuppressWarnings("unchecked")
    public SignatureResponse generateSignature() {
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, Object> paramsToSign = ObjectUtils.asMap(
            "timestamp", timestamp,
            "folder", UPLOAD_FOLDER
        );
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);
        log.debug("Generated Cloudinary signature for timestamp: {}", timestamp);
        return SignatureResponse.builder()
            .signature(signature)
            .timestamp(timestamp)
            .cloudName(cloudName)
            .apiKey(apiKey)
            .folder(UPLOAD_FOLDER)
            .build();
    }

}
