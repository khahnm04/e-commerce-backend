package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.PermissionResponse;
import com.khahnm18.ecommercebackend.service.auth.PermissionService;
import com.khahnm18.ecommercebackend.service.auth.cache.PermissionCacheService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "PERMISSION-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Controller", description = "Permission management APIs")
public class AdminPermissionController {

    private final Translator translator;
    private final PermissionService permissionService;
    private final PermissionCacheService permissionCacheService;

    @GetMapping
    @Operation(summary = "Get all permissions")
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        log.info("Get all permissions");
        return ApiResponse.<List<PermissionResponse>>builder()
            .status(HttpStatus.OK.value())
            .data(permissionService.getAllPermissions())
            .message(translator.toLocale("permission.list.success"))
            .build();
    }

    @GetMapping("/my-permissions")
    @Operation(summary = "Get my permissions")
    public ApiResponse<List<String>> getMyPermissions() {
        log.info("Get my permissions");
        UUID currentAccountId = SecurityUtil.getCurrentAccount();
        boolean isAdmin = SecurityUtil.isAdmin();
        return ApiResponse.<List<String>>builder()
            .status(HttpStatus.OK.value())
            .data(permissionCacheService.getPermissions(currentAccountId, isAdmin))
            .message(translator.toLocale("permission.my-list.success"))
            .build();
    }

}
