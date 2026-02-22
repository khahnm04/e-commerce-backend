package com.khahnm04.ecommerce.modules.branch.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.branch.dto.request.*;
import com.khahnm04.ecommerce.modules.branch.dto.response.BranchResponse;
import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import com.khahnm04.ecommerce.modules.branch.enums.BranchSortField;
import com.khahnm04.ecommerce.modules.branch.enums.BranchStatus;
import com.khahnm04.ecommerce.modules.branch.mapper.BranchMapper;
import com.khahnm04.ecommerce.modules.branch.mapper.BranchMyBatisMapper;
import com.khahnm04.ecommerce.modules.branch.repository.BranchRepository;
import com.khahnm04.ecommerce.modules.branch.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final BranchMyBatisMapper branchMyBatisMapper;

    @Override
    @Transactional
    public BranchResponse createBranch(BranchRequest request) {
        if (branchRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.BRANCH_EXISTED);
        }
        if (branchRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BRANCH_EXISTED);
        }

        Branch branch = branchMapper.toEntity(request);
        branch.setStatus(request.getStatus() == null ? BranchStatus.ACTIVE : request.getStatus());

        Branch savedBranch = branchRepository.save(branch);
        log.info("Created branch id: {}", savedBranch.getId());
        return branchMapper.toResponse(savedBranch);
    }

    @Override
    public PageResponse<BranchResponse> getAllBranches(BranchFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<BranchResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<BranchResponse> getAllDeletedBranches(BranchFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<BranchResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
    }

    @Override
    public BranchResponse getBranchDetailById(Long id) {
        return branchMapper.toResponse(getBranchById(id));
    }

    @Override
    public BranchResponse getBranchDetailByCode(String code) {
        Branch branch = branchRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponse updateBranch(Long id, BranchRequest request) {
        Branch branch = getBranchById(id);

        if (StringUtils.hasText(request.getCode())
                && branchRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }
        if (StringUtils.hasText(request.getName())
                && branchRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        branchMapper.updateBranch(branch, request);

        Branch savedBranch = branchRepository.save(branch);
        log.info("Updated branch id: {}", savedBranch.getId());
        return branchMapper.toResponse(savedBranch);
    }

    @Override
    @Transactional
    public void updateBranchStatus(Long id, BranchUpdateStatusRequest request) {
        Branch branch = getBranchById(id);

        if (branch.getStatus() == request.getStatus()) {
            return;
        }

        branch.setStatus(request.getStatus());
        branchRepository.save(branch);
        log.info("Changed status for branch id {} to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteBranch(Long id) {
        branchRepository.delete(getBranchById(id));
        log.info("Soft deleted branch id {}", id);
    }

    @Override
    @Transactional
    public void restoreBranch(Long id) {
        branchRepository.findDeletedBranchById(id)
                .ifPresentOrElse(branch -> {
                    if (branch.getDeletedAt() == null) {
                        log.warn("Branch with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    branch.setDeletedAt(null);
                    branchRepository.save(branch);
                    log.info("Restored branch id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.BRANCH_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(BranchBulkActionRequest request) {
        Set<Long> branchIds = request.getBranches();
        if (branchIds == null || branchIds.isEmpty()) {
            throw new AppException(ErrorCode.BRANCH_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<Branch> existingBranches = new HashSet<>(
                action.equals("RESTORE")
                        ? branchRepository.findAllDeletedByIds(branchIds)
                        : branchRepository.findAllById(branchIds));

        Set<Long> existingBranchIds = existingBranches.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (branchIds.size() != existingBranchIds.size()) {
            Set<Long> missingIds = new HashSet<>(branchIds);
            missingIds.removeAll(existingBranchIds);
            log.error("Bulk action failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.BRANCH_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                BranchStatus status = BranchStatus.valueOf(action);
                branchRepository.updateStatusByIds(status, existingBranchIds);
                log.info("Bulk update status {} branches ids: {}", action, existingBranchIds);
            }
            case "DELETE" -> {
                branchRepository.softDeleteByIds(LocalDateTime.now(), existingBranchIds);
                log.info("Bulk delete branches ids: {}", existingBranchIds);
            }
            case "RESTORE" -> {
                branchRepository.restoreDeletedByIds(existingBranchIds);
                log.info("Bulk restore branches ids: {}", existingBranchIds);
            }
            default -> throw new AppException(ErrorCode.BRANCH_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} branches", action, existingBranchIds.size());
    }

    // ======================== Private methods ========================

    /**
     * @param request   Filter and search parameters
     * @param pageable  Pagination information
     * @param isDeleted true for deleted branches, false for active
     * @return Page of BranchResponse
     */
    private Page<BranchResponse> getAllWithFilterAndSearch(BranchFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        // Parse sort
        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), BranchSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<BranchResponse> content = branchMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = branchMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
