package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.auth.PermissionResponse;
import com.khahnm04.ecommerce.service.auth.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Slf4j(topic = "PERMISSION-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/permissions")
@RequiredArgsConstructor
@Tag(name = "Admin Permission Controller", description = "APIs for managing permissions")
public class AdminPermissionController {

    private final PermissionService permissionService;
    private final Translator translator;

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        log.info("Get all permissions");
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermissions())
                .message(translator.toLocale("PERMISSION_GET_ALL_SUCCESS"))
                .build();
    }

}
