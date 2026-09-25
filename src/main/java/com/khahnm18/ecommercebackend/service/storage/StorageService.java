package com.khahnm18.ecommercebackend.service.storage;

import com.khahnm18.ecommercebackend.dto.response.storage.PresignedResponse;

public interface StorageService {

    PresignedResponse generatePresignedUrl(String folder, String fileName);

}
