package com.khahnm04.ecommerce.modules.inventory.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import com.khahnm04.ecommerce.modules.branch.service.BranchService;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseBulkActionRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.WarehouseUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.WarehouseResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.Warehouse;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseSortField;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseType;
import com.khahnm04.ecommerce.modules.inventory.mapper.WarehouseMapper;
import com.khahnm04.ecommerce.modules.inventory.mapper.mybatis.WarehouseMyBatisMapper;
import com.khahnm04.ecommerce.modules.inventory.repository.WarehouseRepository;
import com.khahnm04.ecommerce.modules.inventory.service.WarehouseService;
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
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final WarehouseMyBatisMapper warehouseMyBatisMapper;
    private final BranchService branchService;

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        if (warehouseRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.WAREHOUSE_EXISTED);
        }
        if (warehouseRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.WAREHOUSE_EXISTED);
        }

        Branch branch = branchService.getBranchById(request.getBranchId());

        Warehouse warehouse = warehouseMapper.toWarehouse(request);
        warehouse.setBranch(branch);
        warehouse.setStatus(request.getStatus() == null ? WarehouseStatus.ACTIVE : request.getStatus());
        warehouse.setType(request.getType() == null ? WarehouseType.MAIN : request.getType());

        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Created warehouse id: {}", saved.getId());
        return warehouseMapper.toWarehouseResponse(saved);
    }

    @Override
    public PageResponse<WarehouseResponse> getAllWarehouses(WarehouseFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<WarehouseResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<WarehouseResponse> getAllDeletedWarehouses(WarehouseFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<WarehouseResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));
    }

    @Override
    public WarehouseResponse getWarehouseDetailById(Long id) {
        return warehouseMapper.toWarehouseResponse(getWarehouseById(id));
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
        Warehouse warehouse = getWarehouseById(id);

        if (StringUtils.hasText(request.getCode())
                && warehouseRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }
        if (StringUtils.hasText(request.getName())
                && warehouseRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        if (request.getBranchId() != null) {
            Branch branch = branchService.getBranchById(request.getBranchId());
            warehouse.setBranch(branch);
        }

        warehouseMapper.updateWarehouse(warehouse, request);

        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Updated warehouse id: {}", saved.getId());
        return warehouseMapper.toWarehouseResponse(saved);
    }

    @Override
    @Transactional
    public void updateWarehouseStatus(Long id, WarehouseUpdateStatusRequest request) {
        Warehouse warehouse = getWarehouseById(id);

        if (warehouse.getStatus() == request.getStatus()) {
            return;
        }

        warehouse.setStatus(request.getStatus());
        warehouseRepository.save(warehouse);
        log.info("Changed status for warehouse id {} to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteWarehouse(Long id) {
        warehouseRepository.delete(getWarehouseById(id));
        log.info("Soft deleted warehouse id {}", id);
    }

    @Override
    @Transactional
    public void restoreWarehouse(Long id) {
        warehouseRepository.findDeletedWarehouseById(id)
                .ifPresentOrElse(warehouse -> {
                    if (warehouse.getDeletedAt() == null) {
                        log.warn("Warehouse with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    warehouse.setDeletedAt(null);
                    warehouseRepository.save(warehouse);
                    log.info("Restored warehouse id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.WAREHOUSE_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(WarehouseBulkActionRequest request) {
        Set<Long> warehouseIds = request.getWarehouses();
        if (warehouseIds == null || warehouseIds.isEmpty()) {
            throw new AppException(ErrorCode.WAREHOUSE_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<Warehouse> existingWarehouses = new HashSet<>(
                action.equals("RESTORE")
                        ? warehouseRepository.findAllDeletedByIds(warehouseIds)
                        : warehouseRepository.findAllById(warehouseIds));

        Set<Long> existingIds = existingWarehouses.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (warehouseIds.size() != existingIds.size()) {
            Set<Long> missingIds = new HashSet<>(warehouseIds);
            missingIds.removeAll(existingIds);
            log.error("Bulk action warehouses failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.WAREHOUSE_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                WarehouseStatus status = WarehouseStatus.valueOf(action);
                warehouseRepository.updateStatusByIds(status, existingIds);
                log.info("Bulk update status {} warehouses ids: {}", action, existingIds);
            }
            case "DELETE" -> {
                warehouseRepository.softDeleteByIds(LocalDateTime.now(), existingIds);
                log.info("Bulk delete warehouses ids: {}", existingIds);
            }
            case "RESTORE" -> {
                warehouseRepository.restoreDeletedByIds(existingIds);
                log.info("Bulk restore warehouses ids: {}", existingIds);
            }
            default -> throw new AppException(ErrorCode.WAREHOUSE_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} warehouses", action, existingIds.size());
    }

    // ======================== Private methods ========================

    private Page<WarehouseResponse> getAllWithFilterAndSearch(WarehouseFilterRequest request, Pageable pageable, boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), WarehouseSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<WarehouseResponse> content = warehouseMyBatisMapper.findAllWithFilterAndSearch(
                request.getBranchId(),
                request.getType(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = warehouseMyBatisMapper.countWithFilterAndSearch(
                request.getBranchId(),
                request.getType(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
