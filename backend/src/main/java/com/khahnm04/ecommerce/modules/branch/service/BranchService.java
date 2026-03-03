package com.khahnm04.ecommerce.modules.branch.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchBulkActionRequest;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchRequest;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchFilterRequest;
import com.khahnm04.ecommerce.modules.branch.dto.request.BranchUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.branch.dto.response.BranchResponse;
import com.khahnm04.ecommerce.modules.branch.entity.Branch;

public interface BranchService {

    BranchResponse createBranch(BranchRequest request);

    Branch getBranchById(Long id);

    BranchResponse getBranchDetail(String identifier, boolean deleted);

    PageResponse<BranchResponse> getAllBranches(BranchFilterRequest filter);

    PageResponse<BranchResponse> getAllDeletedBranches(BranchFilterRequest filter);

    BranchResponse updateBranch(Long id, BranchRequest request);

    void updateBranchStatus(Long id, BranchUpdateStatusRequest status);

    void softDeleteBranch(Long id);

    void restoreBranch(Long id);

    void handleBulkAction(BranchBulkActionRequest request);

}
