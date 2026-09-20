package com.khahnm18.ecommercebackend.controller;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.cloudinary.SignatureResponse;
import com.khahnm18.ecommercebackend.service.cloudinary.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CLOUDINARY-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/upload")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    @Operation(summary = "Get Cloudinary signature")
    public ApiResponse<SignatureResponse> getUploadSignature() {
        SignatureResponse signatureData = cloudinaryService.generateSignature();
        log.info("Get Cloudinary signature");
        return ApiResponse.<SignatureResponse>builder()
            .message("Signature generated successfully")
            .data(signatureData)
            .build();
    }

}
