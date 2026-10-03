package com.khahnm18.ecommercebackend.controller;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.request.helper.GenerateSlugRequest;
import com.khahnm18.ecommercebackend.dto.response.helper.GenerateSlugResponse;
import com.khahnm18.ecommercebackend.service.helper.HelperService;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "HELPER-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/helper")
@RequiredArgsConstructor
@Tag(name = "Helper Controller", description = "Helper management APIs")
public class HelperController {

    private final HelperService helperService;
    private final Translator translator;

    @PostMapping("/generate-slug")
    @Operation(summary = "Generate Slug")
    ApiResponse<GenerateSlugResponse> generateSlug(
        @Valid @RequestBody GenerateSlugRequest request
    ) {
        log.info("Generate Slug");
        return ApiResponse.<GenerateSlugResponse>builder()
            .status(HttpStatus.OK.value())
            .data(helperService.generateSlug(request))
            .message(translator.toLocale("helper.generate-slug.success"))
            .build();
    }

}
