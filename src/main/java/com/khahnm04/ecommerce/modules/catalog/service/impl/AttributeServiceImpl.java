package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeBulkActionRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeFilterRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.request.AttributeRequest;
import com.khahnm04.ecommerce.modules.catalog.dto.response.AttributeResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Attribute;
import com.khahnm04.ecommerce.modules.catalog.enums.AttributeSortField;
import com.khahnm04.ecommerce.modules.catalog.mapper.AttributeMapper;
import com.khahnm04.ecommerce.modules.catalog.mapper.mybaits.AttributeMyBatisMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.AttributeRepository;
import com.khahnm04.ecommerce.modules.catalog.service.AttributeService;
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
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeMyBatisMapper attributeMyBatisMapper;

    @Override
    @Transactional
    public AttributeResponse createAttribute(AttributeRequest request) {
        if (attributeRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED);
        }
        if (attributeRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED);
        }

        Attribute attribute = attributeMapper.toAttribute(request);

        Attribute savedAttribute = attributeRepository.save(attribute);
        log.info("Created attribute id: {}", savedAttribute.getId());
        return attributeMapper.toAttributeResponse(savedAttribute);
    }

    @Override
    public PageResponse<AttributeResponse> getAllAttributes(AttributeFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<AttributeResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<AttributeResponse> getAllDeletedAttributes(AttributeFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<AttributeResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Attribute getAttributeById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
    }

    @Override
    public AttributeResponse getAttributeDetailById(Long id) {
        return attributeMapper.toAttributeResponse(getAttributeById(id));
    }

    @Override
    public AttributeResponse getAttributeDetailByCode(String code) {
        Attribute attribute = attributeRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        return attributeMapper.toAttributeResponse(attribute);
    }

    @Override
    @Transactional
    public AttributeResponse updateAttribute(Long id, AttributeRequest request) {
        Attribute attribute = this.getAttributeById(id);

        if (StringUtils.hasText(request.getName())
                && attributeRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED);
        }
        if (StringUtils.hasText(request.getCode())
                && attributeRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.ATTRIBUTE_EXISTED);
        }

        attributeMapper.updateAttribute(attribute, request);
        Attribute savedAttribute = attributeRepository.save(attribute);
        log.info("Updated attribute id: {}", savedAttribute.getId());
        return attributeMapper.toAttributeResponse(savedAttribute);
    }

    @Override
    @Transactional
    public void softDeleteAttribute(Long id) {
        Attribute attribute = this.getAttributeById(id);
        if (attribute.getDeletedAt() != null) {
            return;
        }
        attribute.setDeletedAt(LocalDateTime.now());
        attributeRepository.save(attribute);
        log.info("Soft deleted attribute id {}", id);
    }

    @Override
    @Transactional
    public void restoreAttribute(Long id) {
        attributeRepository.findDeletedAttributeById(id)
                .ifPresentOrElse(attribute -> {
                    if (attribute.getDeletedAt() == null) {
                        log.warn("Attribute with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    attribute.setDeletedAt(null);
                    attributeRepository.save(attribute);
                    log.info("Restored attribute id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(AttributeBulkActionRequest request) {
        Set<Long> attributeIds = request.getAttributes();
        if (attributeIds == null || attributeIds.isEmpty()) {
            throw new AppException(ErrorCode.ATTRIBUTE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

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

    private Page<AttributeResponse> getAllWithFilterAndSearch(AttributeFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), AttributeSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<AttributeResponse> content = attributeMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = attributeMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
