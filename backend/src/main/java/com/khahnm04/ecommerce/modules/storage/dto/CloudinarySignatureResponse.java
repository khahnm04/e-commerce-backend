package com.khahnm04.ecommerce.modules.storage.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Response DTO chứa thông tin cần thiết để Client upload trực tiếp lên
 * Cloudinary
 * sử dụng phương thức Signed Upload.
 */
@Getter
@Builder
public class CloudinarySignatureResponse {

    /**
     * Chữ ký được tạo từ API Secret, dùng để xác thực request upload
     */
    private String signature;

    /**
     * Unix timestamp (tính bằng giây) - thời điểm tạo signature
     */
    private long timestamp;

    /**
     * Cloudinary Cloud Name
     */
    private String cloudName;

    /**
     * Cloudinary API Key (public, không phải secret)
     */
    private String apiKey;

    /**
     * Folder đích trên Cloudinary để lưu file
     */
    private String folder;

}
