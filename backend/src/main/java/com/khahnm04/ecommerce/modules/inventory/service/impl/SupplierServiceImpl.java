package com.khahnm04.ecommerce.modules.inventory.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierBulkActionRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SupplierUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SupplierResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.Supplier;
import com.khahnm04.ecommerce.modules.inventory.enums.SupplierSortField;
import com.khahnm04.ecommerce.modules.inventory.enums.SupplierStatus;
import com.khahnm04.ecommerce.modules.inventory.mapper.SupplierMapper;
import com.khahnm04.ecommerce.modules.inventory.mapper.mybatis.SupplierMyBatisMapper;
import com.khahnm04.ecommerce.modules.inventory.repository.SupplierRepository;
import com.khahnm04.ecommerce.modules.inventory.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierMyBatisMapper supplierMyBatisMapper;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        if (supplierRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.SUPPLIER_EXISTED);
        }
        if (supplierRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.SUPPLIER_EXISTED);
        }

        Supplier supplier = supplierMapper.toSupplier(request);
        supplier.setStatus(request.getStatus() == null ? SupplierStatus.ACTIVE : request.getStatus());

        Supplier saved = supplierRepository.save(supplier);
        log.info("Created supplier id: {}", saved.getId());
        return supplierMapper.toSupplierResponse(saved);
    }

    @Override
    public PageResponse<SupplierResponse> getAllSuppliers(SupplierFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<SupplierResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<SupplierResponse> getAllDeletedSuppliers(SupplierFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<SupplierResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
    }

    @Override
    public SupplierResponse getSupplierDetailById(Long id) {
        return supplierMapper.toSupplierResponse(getSupplierById(id));
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = getSupplierById(id);

        if (StringUtils.hasText(request.getCode())
                && supplierRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }
        if (StringUtils.hasText(request.getName())
                && supplierRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        supplierMapper.updateSupplier(supplier, request);

        Supplier saved = supplierRepository.save(supplier);
        log.info("Updated supplier id: {}", saved.getId());
        return supplierMapper.toSupplierResponse(saved);
    }

    @Override
    @Transactional
    public void updateSupplierStatus(Long id, SupplierUpdateStatusRequest request) {
        Supplier supplier = getSupplierById(id);

        if (supplier.getStatus() == request.getStatus()) {
            return;
        }

        supplier.setStatus(request.getStatus());
        supplierRepository.save(supplier);
        log.info("Changed status for supplier id {} to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteSupplier(Long id) {
        supplierRepository.delete(getSupplierById(id));
        log.info("Soft deleted supplier id {}", id);
    }

    @Override
    @Transactional
    public void restoreSupplier(Long id) {
        supplierRepository.findDeletedSupplierById(id)
                .ifPresentOrElse(supplier -> {
                    if (supplier.getDeletedAt() == null) {
                        log.warn("Supplier with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    supplier.setDeletedAt(null);
                    supplierRepository.save(supplier);
                    log.info("Restored supplier id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.SUPPLIER_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(SupplierBulkActionRequest request) {
        Set<Long> supplierIds = request.getSuppliers();
        if (supplierIds == null || supplierIds.isEmpty()) {
            throw new AppException(ErrorCode.SUPPLIER_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<Supplier> existingSuppliers = new HashSet<>(
                action.equals("RESTORE")
                        ? supplierRepository.findAllDeletedByIds(supplierIds)
                        : supplierRepository.findAllById(supplierIds));

        Set<Long> existingIds = existingSuppliers.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (supplierIds.size() != existingIds.size()) {
            Set<Long> missingIds = new HashSet<>(supplierIds);
            missingIds.removeAll(existingIds);
            log.error("Bulk action suppliers failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.SUPPLIER_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                SupplierStatus status = SupplierStatus.valueOf(action);
                supplierRepository.updateStatusByIds(status, existingIds);
                log.info("Bulk update status {} suppliers ids: {}", action, existingIds);
            }
            case "DELETE" -> {
                supplierRepository.softDeleteByIds(LocalDateTime.now(), existingIds);
                log.info("Bulk delete suppliers ids: {}", existingIds);
            }
            case "RESTORE" -> {
                supplierRepository.restoreDeletedByIds(existingIds);
                log.info("Bulk restore suppliers ids: {}", existingIds);
            }
            default -> throw new AppException(ErrorCode.SUPPLIER_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} suppliers", action, existingIds.size());
    }

    // ======================== Private methods ========================

    private Page<SupplierResponse> getAllWithFilterAndSearch(SupplierFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), SupplierSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<SupplierResponse> content = supplierMyBatisMapper.findAllWithFilterAndSearch(
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = supplierMyBatisMapper.countWithFilterAndSearch(
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
