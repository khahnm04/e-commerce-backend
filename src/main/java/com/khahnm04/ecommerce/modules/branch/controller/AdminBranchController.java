package com.khahnm04.ecommerce.modules.branch.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchBulkActionRequest;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchRequest;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchFilterRequest;
import com.khahnm04.ecommerce.modules.branch.dto.response.BranchResponse;
import com.khahnm04.ecommerce.modules.branch.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/branches")
@RequiredArgsConstructor
public class AdminBranchController {

        private final BranchService branchService;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<BranchResponse> createBranch(
                        @Valid @RequestBody BranchRequest request) {
                return ApiResponse.<BranchResponse>builder()
                                .data(branchService.createBranch(request))
                                .message("Branch created successfully")
                                .build();
        }

        @GetMapping("/{identifier}")
        public ApiResponse<BranchResponse> getBranchDetail(
                        @PathVariable String identifier,
                        @RequestParam(defaultValue = "false") boolean deleted) {
                return ApiResponse.<BranchResponse>builder()
                                .data(branchService.getBranchDetail(identifier, deleted))
                                .message("Get branch detail successfully")
                                .build();
        }

        @GetMapping
        public ApiResponse<List<BranchResponse>> getAllBranches(
                        @Valid @ModelAttribute BranchFilterRequest filter) {
                PageResponse<BranchResponse> pageResponse = branchService.getAllBranches(filter);
                return ApiResponse.<List<BranchResponse>>builder()
                                .data(pageResponse.getData())
                                .meta(pageResponse.getMeta())
                                .message("Get all branches successfully")
                                .build();
        }

        @GetMapping("/deleted")
        public ApiResponse<List<BranchResponse>> getAllDeletedBranches(
                        @Valid @ModelAttribute BranchFilterRequest filter) {
                PageResponse<BranchResponse> pageResponse = branchService.getAllDeletedBranches(filter);
                return ApiResponse.<List<BranchResponse>>builder()
                                .data(pageResponse.getData())
                                .meta(pageResponse.getMeta())
                                .message("Get deleted branches successfully")
                                .build();
        }

        @PutMapping("/{id}")
        public ApiResponse<BranchResponse> updateBranch(
                        @PathVariable Long id,
                        @Valid @RequestBody BranchRequest request) {
                return ApiResponse.<BranchResponse>builder()
                                .data(branchService.updateBranch(id, request))
                                .message("Branch updated successfully")
                                .build();
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> softDeleteBranch(
                        @PathVariable Long id) {
                branchService.softDeleteBranch(id);
                return ApiResponse.<Void>builder()
                                .message("Branch soft deleted successfully")
                                .build();
        }

        @PatchMapping("/{id}/restore")
        public ApiResponse<Void> restoreBranch(
                        @PathVariable Long id) {
                branchService.restoreBranch(id);
                return ApiResponse.<Void>builder()
                                .message("Branch restored successfully")
                                .build();
        }

        @PatchMapping("/bulk")
        public ApiResponse<Void> bulkAction(
                        @Valid @RequestBody BranchBulkActionRequest request) {
                branchService.handleBulkAction(request);
                return ApiResponse.<Void>builder()
                                .message("Bulk action executed successfully")
                                .build();
        }

}
