package com.khahnm04.ecommerce.modules.product.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.product.entity.Variant;
import com.khahnm04.ecommerce.modules.product.mapper.VariantMapper;
import com.khahnm04.ecommerce.modules.product.repository.VariantRepository;
import com.khahnm04.ecommerce.modules.product.repository.specification.VariantSpecification;
import com.khahnm04.ecommerce.modules.product.service.VariantService;
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
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final VariantMapper variantMapper;

    @Override
    @Transactional
    public VariantResponse createVariant(VariantRequest request) {
        if (variantRepository.existsByName(request.name())) {
            throw new AppException(ErrorCode.VARIANT_EXISTED);
        }

        Variant variant = variantMapper.toVariant(request);
        Variant savedVariant = variantRepository.save(variant);
        log.info("Created variant with id: {}", savedVariant.getId());
        return variantMapper.toVariantResponse(savedVariant);
    }

    @Override
    public Variant getVariantById(Long id) {
        return variantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));
    }

    @Override
    public VariantResponse getVariantDetail(Long id, boolean deleted) {
        Variant variant = deleted
                ? getDeletedVariantById(id)
                : getVariantById(id);
        return variantMapper.toVariantResponse(variant);
    }

    @Override
    public PageResponse<VariantResponse> getAllVariants(VariantFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<VariantResponse> getAllDeletedVariants(VariantFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public VariantResponse updateVariant(Long id, VariantRequest request) {
        Variant variant = getVariantById(id);

        if (variantRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new AppException(ErrorCode.VARIANT_EXISTED);
        }

        variantMapper.updateVariant(variant, request);
        Variant savedVariant = variantRepository.save(variant);
        log.info("Updated variant with id: {}", savedVariant.getId());
        return variantMapper.toVariantResponse(savedVariant);
    }

    @Override
    @Transactional
    public void softDeleteVariant(Long id) {
        Variant variant = getVariantById(id);
        variant.setDeletedAt(LocalDateTime.now());
        variantRepository.save(variant);
        log.info("Soft-deleted variant with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreVariant(Long id) {
        Variant variant = getDeletedVariantById(id);
        variant.setDeletedAt(null);
        variantRepository.save(variant);
        log.info("Restored variant with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> variantIds, String actionInput) {
        if (variantIds == null || variantIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_BULK_ACTION_REQUIRED_IDS);
        }

        String action = actionInput.toUpperCase();

        Set<Variant> existingVariants = new HashSet<>(
                action.equals("RESTORE")
                        ? variantRepository.findAllDeletedByIds(variantIds)
                        : variantRepository.findAllById(variantIds));

        Set<Long> existingVariantIds = existingVariants.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (variantIds.size() != existingVariantIds.size()) {
            Set<Long> missingIds = new HashSet<>(variantIds);
            missingIds.removeAll(existingVariantIds);
            log.error("Bulk action variant failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.VARIANT_NOT_FOUND);
        }

        switch (action) {
            case "DELETE" -> {
                variantRepository.softDeleteByIds(LocalDateTime.now(), existingVariantIds);
                log.info("Bulk delete variants ids: {}", existingVariantIds);
            }
            case "RESTORE" -> {
                variantRepository.restoreDeletedByIds(existingVariantIds);
                log.info("Bulk restore variants ids: {}", existingVariantIds);
            }
            default -> throw new AppException(ErrorCode.VARIANT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} variants", action, existingVariantIds.size());
    }

    // ======================== Private methods ========================

    private Variant getDeletedVariantById(Long id) {
        return variantRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));
    }

    private PageResponse<VariantResponse> getAllWithFilterAndSearch(VariantFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<Variant> spec = VariantSpecification.buildSpecification(request, isDeleted);
        Page<Variant> page = variantRepository.findAll(spec, pageable);

        List<VariantResponse> content = page.getContent().stream()
                .map(variantMapper::toVariantResponse)
                .collect(Collectors.toList());

        Page<VariantResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }
}
