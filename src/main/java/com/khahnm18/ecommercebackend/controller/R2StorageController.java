package com.khahnm18.ecommercebackend.controller;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.storage.PresignedResponse;
import com.khahnm18.ecommercebackend.service.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "R2STORAGE-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/r2")
@RequiredArgsConstructor
@Tag(name = "R2 Storage Controller", description = "R2 management APIs")
public class R2StorageController {

    private final StorageService storageService;

    @GetMapping("/presigned-url")
    @Operation(summary = "Generate Presigned Url")
    ApiResponse<PresignedResponse> generatePresignedUrl(
        @RequestParam("folder") String folder,
        @RequestParam("fileName") String fileName
    ) {
        var data = storageService.generatePresignedUrl(folder, fileName);
        return ApiResponse.<PresignedResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Presigned URL generated successfully")
            .data(data)
            .build();
    }

}
