package com.khahnm04.ecommerce.modules.storage.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.khahnm04.ecommerce.modules.storage.dto.CloudinarySignatureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    private static final String UPLOAD_FOLDER = "fashion-ecommerce";

    /**
     * Tạo signature cho Signed Upload.
     * Client sẽ dùng thông tin này để upload trực tiếp lên Cloudinary.
     *
     * @return CloudinarySignatureResponse chứa signature và các thông tin cần thiết
     */
    public CloudinarySignatureResponse generateSignature() {
        long timestamp = System.currentTimeMillis() / 1000; // (giây)

        Map<String, Object> paramsToSign = ObjectUtils.asMap(
                "timestamp", timestamp,
                "folder", UPLOAD_FOLDER
        );

        // Create signature
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);

        log.debug("Generated Cloudinary signature for timestamp: {}", timestamp);

        return CloudinarySignatureResponse.builder()
                .signature(signature)
                .timestamp(timestamp)
                .cloudName(cloudName)
                .apiKey(apiKey)
                .folder(UPLOAD_FOLDER)
                .build();
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            var uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap(
                            "folder", "fashion-ecommerce",
                            "resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            log.error("Upload failed: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty())
            return List.of();

        // Chạy song song tất cả các file cùng lúc
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> this.upload(file)))
                .toList();

        // Chờ tất cả xong và gom kết quả lại
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();
    }

}
