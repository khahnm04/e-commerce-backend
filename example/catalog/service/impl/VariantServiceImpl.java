package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.VariantRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.VariantResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Variant;
import com.khahnm04.ecommerce.modules.catalog.repository.specification.VariantSpecification;
import com.khahnm04.ecommerce.modules.catalog.mapper.VariantMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.VariantRepository;
import com.khahnm04.ecommerce.modules.catalog.service.VariantService;
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
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final VariantMapper variantMapper;

    @Override
    @Transactional
    public VariantResponse createVariant(VariantRequest request) {
        if (variantRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.VARIANT_EXISTED);
        }
        if (variantRepository.existsByName(request.getName())) {
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
    public VariantResponse getVariantDetail(String identifier, boolean deleted) {
        Variant variant;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            variant = deleted
                    ? getDeletedVariantById(id)
                    : getVariantById(id);
        } else {
            variant = deleted
                    ? variantRepository.findByCodeAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND))
                    : variantRepository.findByCodeAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));
        }

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
        if (StringUtils.hasText(request.getName())
                && variantRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.VARIANT_EXISTED);
        }
        if (StringUtils.hasText(request.getCode())
                && variantRepository.existsByCodeAndIdNot(request.getCode(), id)) {
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
        if (variant.getDeletedAt() != null) {
            log.warn("Variant with id {} is already soft deleted", id);
            return;
        }
        variant.setDeletedAt(LocalDateTime.now());
        variantRepository.save(variant);
        log.info("Soft-deleted variant with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreVariant(Long id) {
        Variant variant = getDeletedVariantById(id);
        if (variant.getDeletedAt() == null) {
            log.warn("Variant with id {} is not in a deleted state", id);
            return;
        }
        variant.setDeletedAt(null);
        variantRepository.save(variant);
        log.info("Variant restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(VariantBulkActionRequest request) {
        Set<Long> variantIds = request.getVariants();
        if (variantIds == null || variantIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_BULK_ACTION_REQUIRED_IDS);
        }
        String action = request.getAction().toUpperCase();
        Set<Variant> existingVariants = new HashSet<>(
                action.equals("RESTORE")
                        ? variantRepository.findAllDeletedByIds(variantIds)
                        : variantRepository.findAllById(variantIds));
        Set<Long> existingVariantIds = existingVariants.stream()
                .map(BaseEntity::getId).collect(Collectors.toSet());
        if (variantIds.size() != existingVariantIds.size()) {
            Set<Long> missingIds = new HashSet<>(variantIds);
            missingIds.removeAll(existingVariantIds);
            log.error("Bulk action variants failed. Missing Ids: {}", missingIds);
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
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        org.springframework.data.jpa.domain.Specification<Variant> spec = VariantSpecification
                .buildSpecification(request, isDeleted);
        Page<Variant> page = variantRepository.findAll(spec, pageable);

        List<VariantResponse> content = page.getContent().stream()
                .map(variantMapper::toVariantResponse)
                .collect(Collectors.toList());

        Page<VariantResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
