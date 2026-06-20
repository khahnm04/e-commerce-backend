package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.config.Translator;
import com.khahnm04.ecommerce.dto.request.user.*;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.user.AdminProfileResponse;
import com.khahnm04.ecommerce.service.user.AccountAdminService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j(topic = "ACCOUNT-ADMIN-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/admin/account-admins")
@RequiredArgsConstructor
@Tag(name = "Admin Account Admin Controller", description = "APIs for managing account admins")
public class AdminAccountAdminController {

    private final AccountAdminService accountAdminService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create account admin")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_CREATE')")
    ApiResponse<AccountAdminResponse> createAccountAdmin(
        @Valid @RequestBody AccountAdminCreationRequest request
    ) {
        log.info("Create account admin");
        return ApiResponse.<AccountAdminResponse>builder()
            .data(accountAdminService.createAccountAdmin(request))
            .message(translator.toLocale("ACCOUNT_ADMIN_CREATED_SUCCESS"))
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account admin detail")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_VIEW')")
    ApiResponse<AccountAdminResponse> getAccountAdminDetailById(
        @PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean deleted
    ) {
        log.info("Get account admin detail by id");
        return ApiResponse.<AccountAdminResponse>builder()
            .data(accountAdminService.getAccountAdminDetailById(id, deleted))
            .message(translator.toLocale("ACCOUNT_ADMIN_GET_DETAIL_SUCCESS"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all account admins")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_VIEW')")
    PageResponse<AccountAdminResponse> getAllAccountAdmins(
        @Valid @ModelAttribute AccountAdminFilterRequest request
    ) {
        log.info("Get all account admins");
        Page<AccountAdminResponse> page = accountAdminService.getAllAccountAdmins(request);
        return PageResponse.<AccountAdminResponse>builder()
            .data(page.getContent())
            .meta(PageResponse.Meta.fromPage(page))
            .message(translator.toLocale("ACCOUNT_ADMIN_GET_ALL_SUCCESS"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account admin")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_UPDATE')")
    ApiResponse<AccountAdminResponse> updateAccountAdmin(
        @PathVariable Long id,
        @Valid @RequestBody AccountAdminUpdateRequest request
    ) {
        log.info("Update account admin");
        return ApiResponse.<AccountAdminResponse>builder()
            .data(accountAdminService.updateAccountAdmin(id, request))
            .message(translator.toLocale("ACCOUNT_ADMIN_UPDATED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete account admin")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_DELETE')")
    ApiResponse<Void> softDeleteAccountAdmin(
        @PathVariable Long id
    ) {
        log.info("Soft delete account admin");
        accountAdminService.softDeleteAccountAdmin(id);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ACCOUNT_ADMIN_SOFT_DELETED_SUCCESS"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account admin")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_DELETE')")
    ApiResponse<Void> deleteAccountAdmin(
        @PathVariable Long id
    ) {
        log.info("Delete account admin");
        accountAdminService.deleteAccountAdmin(id);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ACCOUNT_ADMIN_DELETED_SUCCESS"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore account admin")
    //@PreAuthorize("hasAuthority('ACCOUNT_ADMIN_DELETE')")
    ApiResponse<Void> restoreAccountAdmin(
        @PathVariable Long id
    ) {
        log.info("Restore account admin");
        accountAdminService.restoreAccountAdmin(id);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ACCOUNT_ADMIN_RESTORED_SUCCESS"))
            .build();
    }

    @PatchMapping("/bulk")
    @Operation(summary = "Bulk action account admins")
    //@PreAuthorize("hasAnyAuthority('ACCOUNT_ADMIN_UPDATE', 'ACCOUNT_ADMIN_DELETE')")
    ApiResponse<Void> bulkAction(
        @RequestBody BulkActionRequest request
    ) {
        accountAdminService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ACCOUNT_ADMIN_BULK_ACTION_SUCCESS"))
            .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get my account admin profile")
    ApiResponse<AdminProfileResponse> getMyProfile() {
        log.info("Get my profile");
        return ApiResponse.<AdminProfileResponse>builder()
            .data(accountAdminService.getMyProfile())
            .message(translator.toLocale("ACCOUNT_ADMIN_GET_MY_PROFILE_SUCCESS"))
            .build();
    }

    @PutMapping("/me/update")
    @Operation(summary = "Update my account admin profile")
    ApiResponse<AdminProfileResponse> updateMyProfile(
        @Valid @RequestBody AdminProfileRequest request
    ) {
        log.info("Update my profile");
        return ApiResponse.<AdminProfileResponse>builder()
            .data(accountAdminService.updateMyProfile(request))
            .message(translator.toLocale("ACCOUNT_ADMIN_UPDATE_MY_PROFILE_SUCCESS"))
            .build();
    }

    @PutMapping("/me/change-password")
    @Operation(summary = "Change password my account admin profile")
    ApiResponse<Void> changePassword(
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        log.info("Change password my profile");
        accountAdminService.changePassword(request);
        return ApiResponse.<Void>builder()
            .message(translator.toLocale("ACCOUNT_ADMIN_UPDATE_MY_PROFILE_SUCCESS"))
            .build();
    }

    @GetMapping("/me/permissions")
    @Operation(summary = "Get my permission codes")
    ApiResponse<List<String>> getMyPermissions() {
        log.info("Get my permissions");
        return ApiResponse.<List<String>>builder()
            .data(accountAdminService.getMyPermissions())
            .message(translator.toLocale("ACCOUNT_ADMIN_GET_MY_PERMISSIONS_SUCCESS"))
            .build();
    }

}
