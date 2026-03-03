package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Variant;
import com.khahnm04.ecommerce.modules.catalog.entity.VariantValue;
import com.khahnm04.ecommerce.modules.catalog.enums.VariantValueSortField;
import com.khahnm04.ecommerce.modules.catalog.mapper.VariantValueMapper;
import com.khahnm04.ecommerce.modules.catalog.mapper.mybaits.VariantValueMyBatisMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.VariantValueRepository;
import com.khahnm04.ecommerce.modules.catalog.service.VariantService;
import com.khahnm04.ecommerce.modules.catalog.service.VariantValueService;
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
public class VariantValueServiceImpl implements VariantValueService {

    private final VariantValueRepository variantValueRepository;
    private final VariantService variantService;
    private final VariantValueMapper variantValueMapper;
    private final VariantValueMyBatisMapper variantValueMyBatisMapper;

    @Override
    @Transactional
    public VariantValueResponse createVariantValue(VariantValueRequest request) {
        Variant variant = variantService.getVariantById(request.getVariantId());

        VariantValue variantValue = variantValueMapper.toVariantValue(request);
        variantValue.setVariant(variant);

        VariantValue saved = variantValueRepository.save(variantValue);
        log.info("Created variant value id: {}", saved.getId());
        return variantValueMapper.toVariantValueResponse(saved);
    }

    @Override
    public PageResponse<VariantValueResponse> getAllVariantValues(VariantValueFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<VariantValueResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<VariantValueResponse> getAllDeletedVariantValues(VariantValueFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<VariantValueResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public VariantValueResponse getVariantValueDetailById(Long id) {
        VariantValue variantValue = getVariantValueById(id);
        return variantValueMapper.toVariantValueResponse(variantValue);
    }

    @Override
    @Transactional
    public VariantValueResponse updateVariantValue(Long id, VariantValueRequest request) {
        VariantValue variantValue = getVariantValueById(id);

        if (request.getVariantId() != null) {
            Variant variant = variantService.getVariantById(request.getVariantId());
            variantValue.setVariant(variant);
        }

        variantValueMapper.updateVariantValue(variantValue, request);
        VariantValue saved = variantValueRepository.save(variantValue);
        log.info("Updated variant value id: {}", saved.getId());
        return variantValueMapper.toVariantValueResponse(saved);
    }

    @Override
    @Transactional
    public void softDeleteVariantValue(Long id) {
        VariantValue variantValue = getVariantValueById(id);
        if (variantValue.getDeletedAt() != null) {
            return;
        }
        variantValue.setDeletedAt(LocalDateTime.now());
        variantValueRepository.save(variantValue);
        log.info("Soft deleted variant value id {}", id);
    }

    @Override
    @Transactional
    public void restoreVariantValue(Long id) {
        variantValueRepository.findDeletedVariantValueById(id)
                .ifPresentOrElse(variantValue -> {
                    if (variantValue.getDeletedAt() == null) {
                        log.warn("Variant value with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    variantValue.setDeletedAt(null);
                    variantValueRepository.save(variantValue);
                    log.info("Restored variant value id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(VariantValueBulkActionRequest request) {
        Set<Long> variantValueIds = request.getVariantValues();
        if (variantValueIds == null || variantValueIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<VariantValue> existingVariantValues = new HashSet<>(
                action.equals("RESTORE")
                        ? variantValueRepository.findAllDeletedByIds(variantValueIds)
                        : variantValueRepository.findAllById(variantValueIds));

        Set<Long> existingVariantValueIds = existingVariantValues.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (variantValueIds.size() != existingVariantValueIds.size()) {
            Set<Long> missingIds = new HashSet<>(variantValueIds);
            missingIds.removeAll(existingVariantValueIds);
            log.error("Bulk action variant values failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
        }

        switch (action) {
            case "DELETE" -> {
                variantValueRepository.softDeleteByIds(LocalDateTime.now(), existingVariantValueIds);
                log.info("Bulk delete variant values ids: {}", existingVariantValueIds);
            }
            case "RESTORE" -> {
                variantValueRepository.restoreDeletedByIds(existingVariantValueIds);
                log.info("Bulk restore variant values ids: {}", existingVariantValueIds);
            }
            default -> throw new AppException(ErrorCode.VARIANT_VALUE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} variant values", action, existingVariantValueIds.size());
    }

    // ======================== Private methods ========================

    private VariantValue getVariantValueById(Long id) {
        return variantValueRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND));
    }

    private Page<VariantValueResponse> getAllWithFilterAndSearch(VariantValueFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), VariantValueSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<VariantValueResponse> content = variantValueMyBatisMapper.findAllWithFilterAndSearch(
                request.getVariantId(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = variantValueMyBatisMapper.countWithFilterAndSearch(
                request.getVariantId(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
