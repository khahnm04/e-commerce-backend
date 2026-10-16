package com.khahnm18.ecommercebackend.controller.admin;

import com.khahnm18.ecommercebackend.enums.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.PageResponse;
import com.khahnm18.ecommercebackend.dto.response.user.UserResponse;
import com.khahnm18.ecommercebackend.service.user.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.UUID;

@Slf4j(topic = "USER-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Account user management APIs")
public class AdminUserController {

    private final Translator translator;
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user detail by id")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    ApiResponse<UserResponse> getUserDetailById(
        @PathVariable("id") UUID accountId,
        @RequestParam(defaultValue = "false") boolean isDeleted
    ) {
        log.info("Get user detail: accountId={}", accountId);
        return ApiResponse.<UserResponse>builder()
            .status(HttpStatus.OK.value())
            .data(userService.getUserDetailById(accountId, isDeleted))
            .message(translator.toLocale("user.detail.success"))
            .build();
    }

    @GetMapping
    @Operation(summary = "Get all users")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    PageResponse<UserResponse> getAllUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean isDeleted,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate,
        @RequestParam(required = false) AccountStatus status
    ) {
        log.info("Get all users");
        Page<UserResponse> userPage = userService.getAllUsers(
            page,
            size,
            keyword,
            isDeleted,
            startDate,
            endDate,
            status
        );
        return PageResponse.<UserResponse>builder()
            .status(HttpStatus.OK.value())
            .data(userPage.getContent())
            .meta(PageResponse.Meta.fromPage(userPage))
            .message(translator.toLocale("user.list.success"))
            .build();
    }

    @PostMapping
    @Operation(summary = "Create account user")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<UserResponse> createUser(
        @Valid @RequestBody UserCreationRequest request
    ) {
        log.info("Create account user");
        return ApiResponse.<UserResponse>builder()
            .status(HttpStatus.CREATED.value())
            .data(userService.createUser(request))
            .message(translator.toLocale("user.create.success"))
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    ApiResponse<UserResponse> updateUser(
        @PathVariable("id") UUID accountId,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        log.info("Update user: accountId={}", accountId);
        return ApiResponse.<UserResponse>builder()
            .status(HttpStatus.OK.value())
            .data(userService.updateUser(accountId, request))
            .message(translator.toLocale("user.update.success"))
            .build();
    }

    @PatchMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete user")
    @PreAuthorize("hasAuthority('USER_TRASH')")
    ApiResponse<Void> softDeleteUser(
        @PathVariable("id") UUID accountId
    ) {
        log.info("Soft delete user: accountId={}", accountId);
        userService.softDeleteUser(accountId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("user.soft-delete.success"))
            .build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore user")
    @PreAuthorize("hasAuthority('USER_TRASH')")
    ApiResponse<Void> restoreUser(
        @PathVariable("id") UUID accountId
    ) {
        log.info("Restore user: accountId={}", accountId);
        userService.restoreUser(accountId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("user.restore.success"))
            .build();
    }

    @PatchMapping("/bulk-action")
    @Operation(summary = "Bulk action user")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'USER_TRASH', 'USER_DELETE')")
    ApiResponse<Void> bulkAction(
        @Valid @RequestBody BulkActionRequest request
    ) {
        userService.bulkAction(request);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("user.bulk-action.success"))
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    ApiResponse<Void> deleteUser(
        @PathVariable("id") UUID accountId
    ) {
        log.info("Delete user: accountId={}", accountId);
        userService.deleteUser(accountId);
        return ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message(translator.toLocale("user.delete.success"))
            .build();
    }

}
