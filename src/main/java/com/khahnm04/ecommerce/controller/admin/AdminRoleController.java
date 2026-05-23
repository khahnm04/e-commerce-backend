package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.filter.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import com.khahnm04.ecommerce.service.auth.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j(topic = "ROLE-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/roles")
@RequiredArgsConstructor
@Tag(name = "Admin Role Controller", description = "APIs for managing roles")
public class AdminRoleController {

    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create role")
    ApiResponse<RoleResponse> createRole(
        @Valid @RequestBody RoleRequest request
    ) {
        log.info("Create role");
        return ApiResponse.<RoleResponse>builder()
            .data(roleService.createRole(request))
            .message("Role created successfully")
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role detail by id")
    ApiResponse<RoleResponse> getRoleDetailById(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean deleted
    ) {
        log.info("Get role detail by id");
        return ApiResponse.<RoleResponse>builder()
            .data(roleService.getRoleDetailById(id, deleted))
            .message("Get role detail by id successfully")
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all roles")
    PageResponse<RoleResponse> getAllRoles(
        @Valid @ModelAttribute RoleFilterRequest request
    ) {
        log.info("Get all roles");
        Page<RoleResponse> page = roleService.getAllRoles(request);
        return PageResponse.<RoleResponse>builder()
            .data(page.getContent())
            .meta(PageResponse.Meta.fromPage(page))
            .message("Get all roles successfully")
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role")
    ApiResponse<RoleResponse> updateRole(
        @PathVariable Long id,
        @Valid @RequestBody RoleRequest request
    ) {
        log.info("Update role");
        return ApiResponse.<RoleResponse>builder()
            .data(roleService.updateRole(id, request))
            .message("Role updated successfully")
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete role")
    ApiResponse<Void> softDeleteRole(
        @PathVariable Long id
    ) {
        log.info("Soft delete role");
        roleService.softDeleteRole(id);
        return ApiResponse.<Void>builder()
            .message("Role soft deleted successfully")
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role")
    ApiResponse<Void> deleteRole(
        @PathVariable Long id
    ) {
        log.info("Delete role");
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
            .message("Role deleted successfully")
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore role")
    ApiResponse<Void> restoreRole(
        @PathVariable Long id
    ) {
        log.info("Restore role");
        roleService.restoreRole(id);
        return ApiResponse.<Void>builder()
            .message("Role restored successfully")
            .build();
    }

    @PatchMapping("/bulk")
    ApiResponse<Void> bulkAction(
        @RequestBody BulkActionRequest request
    ) {
        roleService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .message("bulk action executed successfully")
            .build();
    }

}
