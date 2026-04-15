package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.VariantValueProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.VariantValueFilterRequest;
import com.khahnm04.ecommerce.dto.request.VariantValueRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.VariantValueResponse;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.entity.Variant;
import com.khahnm04.ecommerce.entity.VariantValue;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.VariantValueMapper;
import com.khahnm04.ecommerce.repository.ProductVariantValueRepository;
import com.khahnm04.ecommerce.repository.VariantValueRepository;
import com.khahnm04.ecommerce.service.VariantService;
import com.khahnm04.ecommerce.service.VariantValueService;
import com.khahnm04.ecommerce.specification.VariantValueSpecification;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantValueServiceImpl implements VariantValueService {

    private final VariantValueRepository variantValueRepository;
    private final ProductVariantValueRepository productVariantValueRepository;
    private final VariantValueMapper variantValueMapper;
    private final VariantService variantService;
    private final AuditorAwareConfig auditorAwareConfig;

    @Override
    @Transactional
    public VariantValueResponse createVariantValue(VariantValueRequest request) {
        if (variantValueRepository.existsByVariantIdAndValueAndDeletedAtIsNull(request.variantId(), request.value())) {
            throw new AppException(ErrorCode.VARIANT_VALUE_EXISTED);
        }
        if (variantValueRepository.existsByVariantIdAndValueAndDeletedAtIsNotNull(request.variantId(),
                request.value())) {
            throw new AppException(ErrorCode.VARIANT_VALUE_IN_TRASH);
        }

        Variant variant = variantService.getVariantById(request.variantId());

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
            Variant variant = variantService.getVariantById(request.variantId());
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
        if (variantValue.getDeletedAt() != null) {
            log.warn("Variant Value with id {} is already soft deleted", id);
            return;
        }
        variantValue.setDeletedAt(LocalDateTime.now());
        variantValue.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        variantValueRepository.save(variantValue);
        log.info("Soft-deleted variant value with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteVariantValue(Long id) {
        VariantValue variantValue = getDeletedVariantValueById(id);
        if (variantValue.getDeletedAt() == null) {
            log.warn("Variant Value with id {} is already deleted", id);
            return;
        }
        if (productVariantValueRepository.existsByVariantValueId(id)) {
            throw new AppException(ErrorCode.VARIANT_VALUE_HAS_PRODUCT_VARIANTS);
        }
        variantValueRepository.delete(variantValue);
        log.info("Permanently deleted variant value with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreVariantValue(Long id) {
        VariantValue variantValue = getDeletedVariantValueById(id);
        if (variantValue.getDeletedAt() == null) {
            log.warn("Variant Value with id {} is not in a deleted state", id);
            return;
        }
        variantValue.setDeletedAt(null);
        variantValue.setDeletedBy(null);
        variantValueRepository.save(variantValue);
        log.info("Restored variant value with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> variantValueIds = request.ids();
        String action = request.action().toUpperCase();

        if (variantValueIds == null || variantValueIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "SOFT_DELETE" -> {
                User currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                variantValueRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, variantValueIds);
                log.info("Bulk soft-delete variant values ids: {}", variantValueIds);
            }
            case "DELETE" -> {
                if (productVariantValueRepository.existsByVariantValueIdIn(variantValueIds)) {
                    throw new AppException(ErrorCode.VARIANT_VALUE_HAS_PRODUCT_VARIANTS);
                }
                List<VariantValue> variantValues = variantValueRepository.findAllDeletedByIds(variantValueIds);
                variantValueRepository.deleteAll(variantValues);
                log.info("Bulk permanently delete variant values ids: {}", variantValueIds);
            }
            case "RESTORE" -> {
                variantValueRepository.restoreDeletedByIds(variantValueIds);
                log.info("Bulk restore variant values ids: {}", variantValueIds);
            }
            default -> throw new AppException(ErrorCode.VARIANT_VALUE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} variant values", action, variantValueIds.size());
    }

    // ======================== Private methods ========================

    private VariantValue getDeletedVariantValueById(Long id) {
        return variantValueRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND));
    }

    private PageResponse<VariantValueResponse> getAllWithFilterAndSearch(VariantValueFilterRequest request,
            boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<VariantValue> spec = VariantValueSpecification.buildSpecification(request, isDeleted);
        Page<VariantValueProjection> page = variantValueRepository
                .findBy(spec, q -> q.as(VariantValueProjection.class).page(pageable));

        List<VariantValueResponse> content = page.getContent().stream()
                .map(proj -> VariantValueResponse.builder()
                        .id(proj.getId())
                        .variant(proj.getVariantName())
                        .variantId(proj.getVariantId())
                        .value(proj.getValue())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<VariantValueResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
