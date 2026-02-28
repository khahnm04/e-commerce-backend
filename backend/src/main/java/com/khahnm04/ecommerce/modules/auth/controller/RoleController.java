package com.khahnm04.ecommerce.modules.auth.controller;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleBulkActionRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleFilterRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleRequest;
import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;
import com.khahnm04.ecommerce.modules.auth.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<RoleResponse> createRole(
            @Valid @RequestBody RoleRequest request
    ) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .message("role created successfully")
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<RoleResponse> getRoleById(
        @PathVariable Long id
    ) {
        return ApiResponse.<RoleResponse>builder()
            .data(roleService.getRoleDetailById(id))
            .message("get role detail successfully")
            .build();
    }

    @GetMapping("/deleted/{id}")
    ApiResponse<RoleResponse> getDeletedRoleById(
        @PathVariable Long id
    ) {
        return ApiResponse.<RoleResponse>builder()
            .data(roleService.getDeletedRoleDetailById(id))
            .message("get deleted role detail successfully")
            .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles(
            @Valid @ModelAttribute RoleFilterRequest filter
    ) {
        PageResponse<RoleResponse> pageResponse = roleService.getAllRoles(filter);
        return ApiResponse.<List<RoleResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("get all roles successfully")
                .build();
    }

    @GetMapping("/deleted")
    ApiResponse<List<RoleResponse>> getAllDeletedRoles(
            @Valid @ModelAttribute RoleFilterRequest filter
    ) {
        PageResponse<RoleResponse> pageResponse = roleService.getAllDeletedRoles(filter);
        return ApiResponse.<List<RoleResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("get deleted roles successfully")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<RoleResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequest request
    ) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.updateRole(id, request))
                .message("role updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> softDeleteRole(
            @PathVariable Long id
    ) {
        roleService.softDeleteRole(id);
        return ApiResponse.<Void>builder()
                .message("role soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    ApiResponse<Void> restoreRole(
            @PathVariable Long id
    ) {
        roleService.restoreRole(id);
        return ApiResponse.<Void>builder()
                .message("role restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    ApiResponse<Void> bulkAction(
            @Valid @RequestBody RoleBulkActionRequest request
    ) {
        roleService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("bulk action executed successfully")
                .build();
    }

}
