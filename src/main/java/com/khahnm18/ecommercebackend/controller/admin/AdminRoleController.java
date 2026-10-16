package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import com.khahnm18.ecommercebackend.service.auth.RoleService;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "ROLE-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
@Tag(name = "Role Controller", description = "Role management APIs")
public class AdminRoleController {

    private final RoleService roleService;
    private final Translator translator;

    @GetMapping("/{id}")
    @Operation(summary = "Get role detail by id")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    ApiResponse<RoleResponse> getRoleDetailById(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get role detail: roleId={}", id);
        return ApiResponse.<RoleResponse>builder()
            .status(HttpStatus.OK.value())
            .data(roleService.getRoleDetailById(id, isDeleted))
            .message(translator.toLocale("role.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all roles")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    PageResponse<RoleResponse> getAllRoles(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean isDeleted,
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate
    ) {
        log.info("Get all roles");
        Page<RoleResponse> rolePage = roleService.getAllRoles(
            page,
            size,
            sort,
            keyword,
            isDeleted,
            startDate,
            endDate
        );
        return PageResponse.<RoleResponse>builder()
            .status(HttpStatus.OK.value())
            .data(rolePage.getContent())
            .meta(PageResponse.Meta.fromPage(rolePage))
            .message(translator.toLocale("role.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create role")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    ApiResponse<RoleResponse> createRole(
        @Valid @RequestBody RoleRequest request
    ) {
        log.info("Create role");
        return ApiResponse.<RoleResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(roleService.createRole(request))
            .message(translator.toLocale("role.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    ApiResponse<RoleResponse> updateRole(
        @PathVariable UUID id,
        @Valid @RequestBody RoleRequest request
    ) {
        log.info("Update role: roleId={}", id);
        return ApiResponse.<RoleResponse>builder()
            .status(HttpStatus.OK.value())
            .data(roleService.updateRole(id, request))
            .message(translator.toLocale("role.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete role")
    @PreAuthorize("hasAuthority('ROLE_TRASH')")
    ApiResponse<Void> softDeleteRole(
        @PathVariable UUID id
    ) {
        log.info("Soft delete role: roleId={}", id);
        roleService.softDeleteRole(id);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("role.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore role")
    @PreAuthorize("hasAuthority('ROLE_TRASH')")
    ApiResponse<Void> restoreRole(
        @PathVariable UUID id
    ) {
        log.info("Restore role: roleId={}", id);
        roleService.restoreRole(id);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("role.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action role")
    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE', 'ROLE_TRASH', 'ROLE_DELETE')")
    ApiResponse<Void> deleteRole(
        @Valid @RequestBody BulkActionRequest request
    ) {
        log.info("Bulk action role: roleIds={}", request.ids());
        roleService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("role.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    ApiResponse<Void> deleteRole(
        @PathVariable UUID id
    ) {
        log.info("Delete role: roleId={}", id);
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("role.delete.success"))
            .build();
    }

}
