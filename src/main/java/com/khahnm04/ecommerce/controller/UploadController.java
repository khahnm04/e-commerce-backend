package com.khahnm04.ecommerce.controller;

import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.upload.CloudinarySignatureResponse;
import com.khahnm04.ecommerce.service.upload.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    public ApiResponse<CloudinarySignatureResponse> getUploadSignature() {
        CloudinarySignatureResponse signatureData = cloudinaryService.generateSignature();
        return ApiResponse.<CloudinarySignatureResponse>builder()
                .message("Signature generated successfully")
                .data(signatureData)
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> uploadFiles(
            @RequestPart("files") List<MultipartFile> files
    ) {
        List<String> urls = cloudinaryService.uploadFiles(files);
        return ApiResponse.<List<String>>builder()
                .message("Upload " + urls.size() + " files successfully")
                .data(urls)
                .build();
    }

}
