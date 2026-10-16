package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.request.website.WebsiteInfoRequest;
import com.khahnm18.ecommercebackend.dto.response.website.WebsiteInfoResponse;
import com.khahnm18.ecommercebackend.service.website.WebsiteInfoService;
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

@Slf4j(topic = "WEBSITE-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/website-info")
@RequiredArgsConstructor
@Tag(name = "Website Info Controller", description = "Website Info management APIs")
public class AdminWebsiteController {

    private final WebsiteInfoService websiteInfoService;
    private final Translator translator;

    @GetMapping
    @Operation(summary = "Get website info")
    @PreAuthorize("hasAuthority('WEBSITE_INFO_UPDATE')")
    ApiResponse<WebsiteInfoResponse> getWebsiteInfo() {
        log.info("Get website info");
        return ApiResponse.<WebsiteInfoResponse>builder()
            .status(HttpStatus.OK.value())
            .data(websiteInfoService.getWebsiteInfo())
            .message(translator.toLocale("website-info.detail.success"))
            .build();
    }

    @GetMapping("/history")
    @Operation(summary = "Get website info history")
    @PreAuthorize("hasAuthority('WEBSITE_INFO_VIEW')")
    ApiResponse<List<WebsiteInfoResponse>> getWebsiteInfoHistory() {
        log.info("Get website info history");
        return ApiResponse.<List<WebsiteInfoResponse>>builder()
            .status(HttpStatus.OK.value())
            .data(websiteInfoService.getWebsiteInfoHistory())
            .message(translator.toLocale("website-info.history.success"))
            .build();
    }

    @PutMapping
    @Operation(summary = "Update website info")
    @PreAuthorize("hasAuthority('WEBSITE_INFO_UPDATE')")
    ApiResponse<WebsiteInfoResponse> updateWebsiteInfo(
        @Valid @RequestBody WebsiteInfoRequest request
    ) {
        log.info("Update website info");
        return ApiResponse.<WebsiteInfoResponse>builder()
            .status(HttpStatus.OK.value())
            .data(websiteInfoService.updateWebsiteInfo(request))
            .message(translator.toLocale("website-info.update.success"))
            .build();
    }

}
