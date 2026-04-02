package com.khahnm04.ecommerce.service.product.impl;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantValueFilterRequest;
import com.khahnm04.ecommerce.dto.request.product.VariantValueRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.product.VariantValueResponse;
import com.khahnm04.ecommerce.entity.product.Variant;
import com.khahnm04.ecommerce.entity.product.VariantValue;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.product.VariantValueMapper;
import com.khahnm04.ecommerce.repository.product.VariantRepository;
import com.khahnm04.ecommerce.repository.product.VariantValueRepository;
import com.khahnm04.ecommerce.service.product.VariantValueService;
import com.khahnm04.ecommerce.specification.product.VariantValueSpecification;
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
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> variantValueIds = request.ids();
        String action = request.action().toUpperCase();

        if (variantValueIds == null || variantValueIds.isEmpty()) {
            throw new AppException(ErrorCode.VARIANT_VALUE_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "DELETE" -> {
                variantValueRepository.softDeleteByIds(LocalDateTime.now(), variantValueIds);
                log.info("Bulk delete variant values ids: {}", variantValueIds);
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
