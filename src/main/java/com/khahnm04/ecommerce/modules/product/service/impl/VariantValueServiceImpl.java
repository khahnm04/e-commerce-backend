package com.khahnm04.ecommerce.modules.product.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.modules.product.entity.Variant;
import com.khahnm04.ecommerce.modules.product.entity.VariantValue;
import com.khahnm04.ecommerce.modules.product.mapper.VariantValueMapper;
import com.khahnm04.ecommerce.modules.product.repository.VariantRepository;
import com.khahnm04.ecommerce.modules.product.repository.VariantValueRepository;
import com.khahnm04.ecommerce.modules.product.repository.specification.VariantValueSpecification;
import com.khahnm04.ecommerce.modules.product.service.VariantValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final VariantRepository variantRepository;
    private final VariantValueMapper variantValueMapper;

    @Override
    @Transactional
    public VariantValueResponse createVariantValue(VariantValueRequest request) {
        if (variantValueRepository.existsByVariantIdAndValue(request.variantId(), request.value())) {
            throw new AppException(ErrorCode.VARIANT_VALUE_EXISTED);
        }

        Variant variant = variantRepository.findByIdAndDeletedAtIsNull(request.variantId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));

        VariantValue variantValue = variantValueMapper.toVariantValue(request);
        variantValue.setVariant(variant);

        VariantValue savedVariantValue = variantValueRepository.save(variantValue);
        log.info("Created variant value with id: {}", savedVariantValue.getId());
        return variantValueMapper.toVariantValueResponse(savedVariantValue);
    }

    @Override
    public VariantValue getVariantValueById(Long id) {
        return variantValueRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND));
    }

    @Override
    public VariantValueResponse getVariantValueDetail(Long id, boolean deleted) {
        VariantValue variantValue = deleted
                ? getDeletedVariantValueById(id)
                : getVariantValueById(id);
        return variantValueMapper.toVariantValueResponse(variantValue);
    }

    @Override
    public PageResponse<VariantValueResponse> getAllVariantValues(VariantValueFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<VariantValueResponse> getAllDeletedVariantValues(VariantValueFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public VariantValueResponse updateVariantValue(Long id, VariantValueRequest request) {
        VariantValue variantValue = getVariantValueById(id);

        if (variantValueRepository.existsByVariantIdAndValueAndIdNot(request.variantId(), request.value(), id)) {
            throw new AppException(ErrorCode.VARIANT_VALUE_EXISTED);
        }

        if (!variantValue.getVariant().getId().equals(request.variantId())) {
            Variant variant = variantRepository.findByIdAndDeletedAtIsNull(request.variantId())
                    .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));
            variantValue.setVariant(variant);
        }

        variantValueMapper.updateVariantValue(variantValue, request);
        VariantValue savedVariantValue = variantValueRepository.save(variantValue);
        log.info("Updated variant value with id: {}", savedVariantValue.getId());
        return variantValueMapper.toVariantValueResponse(savedVariantValue);
    }

    @Override
    @Transactional
    public void softDeleteVariantValue(Long id) {
        VariantValue variantValue = getVariantValueById(id);
        variantValue.setDeletedAt(LocalDateTime.now());
        variantValueRepository.save(variantValue);
        log.info("Soft-deleted variant value with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreVariantValue(Long id) {
        VariantValue variantValue = getDeletedVariantValueById(id);
        variantValue.setDeletedAt(null);
        variantValueRepository.save(variantValue);
        log.info("Restored variant value with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> variantValueIds, String actionInput) {
        if (variantValueIds == null || variantValueIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = actionInput.toUpperCase();

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

    private VariantValue getDeletedVariantValueById(Long id) {
        return variantValueRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND));
    }

    private PageResponse<VariantValueResponse> getAllWithFilterAndSearch(VariantValueFilterRequest request,
            boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<VariantValue> spec = VariantValueSpecification.buildSpecification(request, isDeleted);
        Page<VariantValue> page = variantValueRepository.findAll(spec, pageable);

        List<VariantValueResponse> content = page.getContent().stream()
                .map(variantValueMapper::toVariantValueResponse)
                .collect(Collectors.toList());

        Page<VariantValueResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
