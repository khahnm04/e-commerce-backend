package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.filter.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.request.RoleRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.RoleResponse;
import com.khahnm04.ecommerce.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/roles")
@RequiredArgsConstructor
public class AdminRoleController {

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
    ApiResponse<RoleResponse> getRoleDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.getRoleDetail(id, deleted))
                .message("get role detail successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles(
            @Valid @ModelAttribute RoleFilterRequest filter
    ) {
        PageResponse<RoleResponse> pageResponse = roleService.getAllRoles(filter);
        return ApiResponse.<List<RoleResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("get all roles successfully")
                .build();
    }

    @GetMapping("/deleted")
    ApiResponse<List<RoleResponse>> getAllDeletedRoles(
            @Valid @ModelAttribute RoleFilterRequest filter
    ) {
        PageResponse<RoleResponse> pageResponse = roleService.getAllDeletedRoles(filter);
        return ApiResponse.<List<RoleResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
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

    @PatchMapping("/{id}/soft-delete")
    ApiResponse<Void> softDeleteRole(
            @PathVariable Long id
    ) {
        roleService.softDeleteRole(id);
        return ApiResponse.<Void>builder()
                .message("role soft deleted successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteRole(
            @PathVariable Long id
    ) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
            .message("role deleted successfully")
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
        @RequestBody BulkActionRequest request
    ) {
        roleService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
            .message("bulk action executed successfully")
            .build();
    }

}
