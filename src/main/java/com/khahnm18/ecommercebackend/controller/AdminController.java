package com.khahnm18.ecommercebackend.controller;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminProfileResponse;
import com.khahnm18.ecommercebackend.dto.response.user.AdminResponse;
import com.khahnm18.ecommercebackend.service.user.AdminService;
import com.khahnm18.ecommercebackend.util.Translator;
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
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "ADMIN-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/admins")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Account admin and admin profile management APIs")
public class AdminController {

    private final Translator translator;
    private final AdminService adminService;

    @GetMapping("/{id}")
    @Operation(summary = "Get admin detail by id")
    ApiResponse<AdminResponse> getAdminDetailById(
        @PathVariable UUID accountId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get admin detail: accountId={}", accountId);
        return ApiResponse.<AdminResponse>builder()
            .status(HttpStatus.OK.value())
            .data(adminService.getAdminDetailById(accountId, isDeleted))
            .message(translator.toLocale("admin.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all admins")
    PageResponse<AdminResponse> getAllAdmins(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean isDeleted,
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate
    ) {
        log.info("Get all admins");
        Page<AdminResponse> adminPage = adminService.getAllAdmins(
            page,
            size,
            sort,
            keyword,
            isDeleted,
            startDate,
            endDate
        );
        return PageResponse.<AdminResponse>builder()
            .status(HttpStatus.OK.value())
            .data(adminPage.getContent())
            .meta(PageResponse.Meta.fromPage(adminPage))
            .message(translator.toLocale("admin.list.success"))
            .build();
    }

    @GetMapping("/my-profile")
    @Operation(summary = "Get my account admin profile")
    ApiResponse<AdminProfileResponse> getMyProfile() {
        log.info("Get my profile");
        return ApiResponse.<AdminProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .data(adminService.getMyProfile())
            .message(translator.toLocale("admin.my-profile.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create account admin and admin profile")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<AdminResponse> createAccount(
        @Valid @RequestBody AdminCreationRequest request
    ) {
        log.info("Create account admin and admin profile");
        return ApiResponse.<AdminResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(adminService.createAdmin(request))
            .message(translator.toLocale("admin.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update admin")
    ApiResponse<AdminResponse> updateAdmin(
        @PathVariable UUID accountId,
        @Valid @RequestBody AdminUpdateRequest request
    ) {
        log.info("Update admin: accountId={}", accountId);
        return ApiResponse.<AdminResponse>builder()
            .status(HttpStatus.OK.value())
            .data(adminService.updateAdmin(accountId, request))
            .message(translator.toLocale("admin.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete admin")
    ApiResponse<Void> softDeleteAdmin(
        @PathVariable("id") UUID accountId
    ) {
        log.info("Soft delete admin: accountId={}", accountId);
        adminService.softDeleteAdmin(accountId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("admin.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore soft-deleted admin")
    ApiResponse<Void> restoreAdmin(
        @PathVariable("id") UUID accountId
    ) {
        log.info("Restore admin: accountId={}", accountId);
        adminService.restoreAdmin(accountId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("admin.restore.success"))
            .build();
    }


    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action admin")
    ApiResponse<Void> bulkAction(
        @Valid @RequestBody BulkActionRequest request
    ) {
        adminService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("admin.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete admin")
    ApiResponse<Void> deleteAdmin(
        @PathVariable("id") UUID accountId
    ) {
        log.info("Delete admin: accountId={}", accountId);
        adminService.deleteAdmin(accountId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("admin.delete.success"))
            .build();
    }

}
