package com.khahnm04.ecommerce.service.product.impl;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.VariantResponse;
import com.khahnm04.ecommerce.entity.product.Variant;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.product.VariantMapper;
import com.khahnm04.ecommerce.repository.product.VariantRepository;
import com.khahnm04.ecommerce.service.product.VariantService;
import com.khahnm04.ecommerce.specification.product.VariantSpecification;
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
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> variantIds = request.ids();
        String action = request.action().toUpperCase();

        if (variantIds == null || variantIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "DELETE" -> {
                variantRepository.softDeleteByIds(LocalDateTime.now(), variantIds);
                log.info("Bulk delete variants ids: {}", variantIds);
            }
            case "RESTORE" -> {
                variantRepository.restoreDeletedByIds(variantIds);
                log.info("Bulk restore variants ids: {}", variantIds);
            }
            default -> throw new AppException(ErrorCode.VARIANT_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} variants", action, variantIds.size());
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
