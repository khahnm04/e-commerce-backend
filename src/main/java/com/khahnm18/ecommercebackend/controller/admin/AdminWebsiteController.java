package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.request.website.WebsiteRequest;
import com.khahnm18.ecommercebackend.dto.response.website.WebsiteResponse;
import com.khahnm18.ecommercebackend.service.website.WebsiteService;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j(topic = "ADMIN-WEBSITE-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/website")
@RequiredArgsConstructor
@Tag(name = "Admin Website Controller", description = "Admin Website Management APIs")
public class AdminWebsiteController {

    private final WebsiteService websiteService;
    private final Translator translator;

    @GetMapping
    @Operation(summary = "Get website info")
    @PreAuthorize("hasAuthority('WEBSITE_UPDATE')")
    ApiResponse<WebsiteResponse> getWebsiteInfo() {
        log.info("Get website info");
        return ApiResponse.<WebsiteResponse>builder()
            .status(HttpStatus.OK.value())
            .data(websiteService.getWebsiteInfo())
            .message(translator.toLocale("website.detail.success"))
            .build();
    }

    @GetMapping("/history")
    @Operation(summary = "Get website history")
    @PreAuthorize("hasAuthority('WEBSITE_VIEW')")
    ApiResponse<List<WebsiteResponse>> getWebsiteInfoHistory() {
        log.info("Get website history");
        return ApiResponse.<List<WebsiteResponse>>builder()
            .status(HttpStatus.OK.value())
            .data(websiteService.getWebsiteInfoHistory())
            .message(translator.toLocale("website.history.success"))
            .build();
    }

    @PutMapping
    @Operation(summary = "Update website")
    @PreAuthorize("hasAuthority('WEBSITE_UPDATE')")
    ApiResponse<WebsiteResponse> updateWebsiteInfo(
        @Valid @RequestBody WebsiteRequest request
    ) {
        log.info("Update website");
        return ApiResponse.<WebsiteResponse>builder()
            .status(HttpStatus.OK.value())
            .data(websiteService.updateWebsiteInfo(request))
            .message(translator.toLocale("website.update.success"))
            .build();
    }

}
