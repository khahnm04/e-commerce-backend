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

        Branch branch = branchMapper.toBranch(request);

        Branch savedBranch = branchRepository.save(branch);
        log.info("Created branch with id: {}", savedBranch.getId());
        return branchMapper.toBranchResponse(savedBranch);
    }

    @Override
    public Branch getBranchById(Long id) {
        return branchRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
    }

    @Override
    public BranchResponse getBranchDetail(String identifier, boolean deleted) {
        Branch branch;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            branch = deleted
                    ? getDeletedBranchById(id)
                    : getBranchById(id);
        } else {
            branch = deleted
                    ? branchRepository.findByCodeAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND))
                    : branchRepository.findByCodeAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
        }

        return branchMapper.toBranchResponse(branch);
    }

    @Override
    public PageResponse<BranchResponse> getAllBranches(BranchFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<BranchResponse> getAllDeletedBranches(BranchFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
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
        log.info("Updated branch with id: {}", savedBranch.getId());
        return branchMapper.toBranchResponse(savedBranch);
    }

    @Override
    @Transactional
    public void softDeleteBranch(Long id) {
        Branch branch = getBranchById(id);
        if (branch.getDeletedAt() != null) {
            log.warn("Branch with id {} is already soft deleted", id);
            return;
        }
        branch.setDeletedAt(LocalDateTime.now());
        branchRepository.save(branch);
        log.info("Soft-deleted branch with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreBranch(Long id) {
        Branch branch = getDeletedBranchById(id);
        if (branch.getDeletedAt() == null) {
            log.warn("Branch with id {} is not in a deleted state", id);
            return;
        }
        branch.setDeletedAt(null);
        branchRepository.save(branch);
        log.info("Branch restored with id {}", id);
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
            log.error("Bulk action failed. Missing IDs: {}", missingIds);
            throw new AppException(ErrorCode.BRANCH_NOT_FOUND);
        }

        switch (action) {
            case "DELETE" -> {
                branchRepository.softDeleteByIds(LocalDateTime.now(), existingBranchIds);
                log.info("Bulk soft-deleted branches with IDs: {}", existingBranchIds);
            }
            case "RESTORE" -> {
                branchRepository.restoreDeletedByIds(existingBranchIds);
                log.info("Bulk restored branches with IDs: {}", existingBranchIds);
            }
            default -> throw new AppException(ErrorCode.BRANCH_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} branches", action, existingBranchIds.size());
    }

    // ======================== Private methods ========================

    private Branch getDeletedBranchById(Long id) {
        return branchRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
    }

    private PageResponse<BranchResponse> getAllWithFilterAndSearch(BranchFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        // Parse sort
        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), BranchSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<BranchResponse> content = branchMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = branchMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        Page<BranchResponse> page = new PageImpl<>(content, pageable, total);
        return PageResponse.fromPage(page);
    }

}
