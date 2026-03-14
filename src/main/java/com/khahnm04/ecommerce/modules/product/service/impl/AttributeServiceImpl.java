package com.khahnm04.ecommerce.modules.product.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.product.dto.request.AttributeFilterRequest;
import com.khahnm04.ecommerce.modules.product.dto.request.AttributeRequest;
import com.khahnm04.ecommerce.modules.product.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.modules.product.entity.Attribute;
import com.khahnm04.ecommerce.modules.product.mapper.AttributeMapper;
import com.khahnm04.ecommerce.modules.product.repository.AttributeRepository;
import com.khahnm04.ecommerce.modules.product.repository.specification.AttributeSpecification;
import com.khahnm04.ecommerce.modules.product.service.AttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    @Override
    @Transactional
    public AttributeResponse createAttribute(AttributeRequest request) {
        if (attributeRepository.existsByName(request.name())) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED);
        }

        Attribute attribute = attributeMapper.toAttribute(request);
        Attribute savedAttribute = attributeRepository.save(attribute);
        log.info("Created attribute with id: {}", savedAttribute.getId());
        return attributeMapper.toAttributeResponse(savedAttribute);
    }

    @Override
    public Attribute getAttributeById(Long id) {
        return attributeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
    }

    @Override
    public AttributeResponse getAttributeDetail(Long id, boolean deleted) {
        Attribute attribute = deleted
                ? getDeletedAttributeById(id)
                : getAttributeById(id);
        return attributeMapper.toAttributeResponse(attribute);
    }

    @Override
    public PageResponse<AttributeResponse> getAllAttributes(AttributeFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<AttributeResponse> getAllDeletedAttributes(AttributeFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public AttributeResponse updateAttribute(Long id, AttributeRequest request) {
        Attribute attribute = this.getAttributeById(id);

        if (StringUtils.hasText(request.name())
                && attributeRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED);
        }

        attributeMapper.updateAttribute(attribute, request);
        Attribute savedAttribute = attributeRepository.save(attribute);
        log.info("Updated attribute with id: {}", savedAttribute.getId());
        return attributeMapper.toAttributeResponse(savedAttribute);
    }

    @Override
    @Transactional
    public void softDeleteAttribute(Long id) {
        Attribute attribute = this.getAttributeById(id);
        if (attribute.getDeletedAt() != null) {
            log.warn("Attribute with id {} is already soft deleted", id);
            return;
        }
        attribute.setDeletedAt(LocalDateTime.now());
        attributeRepository.save(attribute);
        log.info("Soft-deleted attribute with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreAttribute(Long id) {
        Attribute attribute = getDeletedAttributeById(id);
        if (attribute.getDeletedAt() == null) {
            log.warn("Attribute with id {} is not in a deleted state", id);
            return;
        }
        attribute.setDeletedAt(null);
        attributeRepository.save(attribute);
        log.info("Attribute restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> attributeIds, String actionInput) {
        if (attributeIds == null || attributeIds.isEmpty()) {
            throw new AppException(ErrorCode.ATTRIBUTE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = actionInput.toUpperCase();

        Set<Attribute> existingAttributes = new HashSet<>(
                action.equals("RESTORE")
                        ? attributeRepository.findAllDeletedByIds(attributeIds)
                        : attributeRepository.findAllById(attributeIds));

        Set<Long> existingAttributeIds = existingAttributes.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (attributeIds.size() != existingAttributeIds.size()) {
            Set<Long> missingIds = new HashSet<>(attributeIds);
            missingIds.removeAll(existingAttributeIds);
            log.error("Bulk action attributes failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        switch (action) {
            case "DELETE" -> {
                attributeRepository.softDeleteByIds(LocalDateTime.now(), existingAttributeIds);
                log.info("Bulk delete attributes ids: {}", existingAttributeIds);
            }
            case "RESTORE" -> {
                attributeRepository.restoreDeletedByIds(existingAttributeIds);
                log.info("Bulk restore attributes ids: {}", existingAttributeIds);
            }
            default -> throw new AppException(ErrorCode.ATTRIBUTE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} attributes", action, existingAttributeIds.size());
    }

    // ======================== Private methods ========================

    private Attribute getDeletedAttributeById(Long id) {
        return attributeRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
    }

    private PageResponse<AttributeResponse> getAllWithFilterAndSearch(AttributeFilterRequest request,
            boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<Attribute> spec = AttributeSpecification.buildSpecification(request, isDeleted);
        Page<Attribute> page = attributeRepository.findAll(spec, pageable);

        List<AttributeResponse> content = page.getContent().stream()
                .map(attributeMapper::toAttributeResponse)
                .collect(Collectors.toList());

        Page<AttributeResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
