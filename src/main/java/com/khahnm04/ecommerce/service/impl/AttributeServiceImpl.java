package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.AttributeProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.AttributeFilterRequest;
import com.khahnm04.ecommerce.dto.request.AttributeRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.entity.Attribute;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.AttributeMapper;
import com.khahnm04.ecommerce.repository.AttributeRepository;
import com.khahnm04.ecommerce.repository.ProductAttributeValueRepository;
import com.khahnm04.ecommerce.service.AttributeService;
import com.khahnm04.ecommerce.specification.AttributeSpecification;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final AttributeMapper attributeMapper;
    private final AuditorAwareConfig auditorAwareConfig;

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
        attribute.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        attributeRepository.save(attribute);
        log.info("Soft-deleted attribute with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteAttribute(Long id) {
        Attribute attribute = getDeletedAttributeById(id);
        if (attribute.getDeletedAt() == null) {
            log.warn("Attribute with id {} is already deleted", id);
            return;
        }
        if (productAttributeValueRepository.existsByAttributeId(id)) {
            throw new AppException(ErrorCode.ATTRIBUTE_HAS_PRODUCTS);
        }
        attributeRepository.delete(attribute);
        log.info("Permanently deleted attribute with id: {}", id);
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
        attribute.setDeletedBy(null);
        attributeRepository.save(attribute);
        log.info("Attribute restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> attributeIds = request.ids();
        String action = request.action().toUpperCase();

        if (attributeIds == null || attributeIds.isEmpty()) {
            throw new AppException(ErrorCode.ATTRIBUTE_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "SOFT_DELETE" -> {
                Employee currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                attributeRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, attributeIds);
                log.info("Bulk soft-delete attributes ids: {}", attributeIds);
            }
            case "RESTORE" -> {
                attributeRepository.restoreDeletedByIds(attributeIds);
                log.info("Bulk restore attributes ids: {}", attributeIds);
            }
            case "DELETE" -> {
                if (productAttributeValueRepository.existsByAttributeIdIn(attributeIds)) {
                    throw new AppException(ErrorCode.ATTRIBUTE_HAS_PRODUCTS);
                }
                Set<Attribute> attributes = attributeRepository.findAllDeletedByIds(attributeIds);
                attributeRepository.deleteAll(attributes);
                log.info("Bulk permanently delete attributes ids: {}", attributeIds);
            }
            default -> throw new AppException(ErrorCode.ATTRIBUTE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} attributes", action, attributeIds.size());
    }

    // ======================== Private methods ========================

    private Attribute getDeletedAttributeById(Long id) {
        return attributeRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
    }

    private PageResponse<AttributeResponse> getAllWithFilterAndSearch(AttributeFilterRequest request,
            boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<Attribute> spec = AttributeSpecification.buildSpecification(request, isDeleted);
        Page<AttributeProjection> page = attributeRepository
                .findBy(spec, q -> q.as(AttributeProjection.class).page(pageable));

        List<AttributeResponse> content = page.getContent().stream()
                .map(proj -> AttributeResponse.builder()
                        .id(proj.getId())
                        .name(proj.getName())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<AttributeResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
