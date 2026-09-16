package com.khahnm18.ecommercebackend.controller;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.PermissionResponse;
import com.khahnm18.ecommercebackend.service.auth.PermissionService;
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

@Slf4j(topic = "PERMISSION-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Controller", description = "Permission management APIs")
public class PermissionController {

    private final PermissionService permissionService;
    private final Translator translator;

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

}
