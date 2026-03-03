package com.khahnm04.ecommerce.modules.inventory.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckBatchUpdateRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckDetailUpdateRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.InventoryCheckRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.InventoryCheckDetailResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.InventoryCheckResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.InventoryCheck;
import com.khahnm04.ecommerce.modules.inventory.entity.InventoryCheckDetail;
import com.khahnm04.ecommerce.modules.inventory.entity.Warehouse;
import com.khahnm04.ecommerce.modules.inventory.enums.InventoryCheckSortField;
import com.khahnm04.ecommerce.modules.inventory.enums.InventoryCheckStatus;
import com.khahnm04.ecommerce.modules.inventory.mapper.mybatis.InventoryCheckMyBatisMapper;
import com.khahnm04.ecommerce.modules.inventory.repository.InventoryCheckDetailRepository;
import com.khahnm04.ecommerce.modules.inventory.repository.InventoryCheckRepository;
import com.khahnm04.ecommerce.modules.inventory.repository.SerialNumberRepository;
import com.khahnm04.ecommerce.modules.inventory.repository.WarehouseRepository;
import com.khahnm04.ecommerce.modules.inventory.service.InventoryCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.khahnm04.ecommerce.modules.inventory.entity.SerialNumber;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryCheckServiceImpl implements InventoryCheckService {

    private final InventoryCheckRepository inventoryCheckRepository;
    private final InventoryCheckDetailRepository inventoryCheckDetailRepository;
    private final InventoryCheckMyBatisMapper inventoryCheckMyBatisMapper;
    private final WarehouseRepository warehouseRepository;
    private final SerialNumberRepository serialNumberRepository;

    @Override
    @Transactional
    public InventoryCheckResponse createInventoryCheck(InventoryCheckRequest request) {
        if (inventoryCheckRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.INVENTORY_CHECK_EXISTED);
        }

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        // Create inventory check
        InventoryCheck check = InventoryCheck.builder()
                .code(request.getCode())
                .warehouse(warehouse)
                .checkDate(LocalDateTime.now())
                .checkType(request.getCheckType())
                .status(InventoryCheckStatus.IN_PROGRESS)
                .notes(request.getNotes())
                .build();
        check = inventoryCheckRepository.save(check);

        // Auto-generate details from serials in the warehouse, grouped by product
        // variant
        List<SerialNumber> serials = serialNumberRepository
                .findByStatusAndWarehouseId(SerialNumberStatus.IN_STOCK, warehouse.getId());

        Map<Long, List<SerialNumber>> variantGrouped = serials.stream()
                .collect(Collectors.groupingBy(sn -> sn.getProductVariant().getId()));

        int totalSystemQuantity = 0;
        for (Map.Entry<Long, List<SerialNumber>> entry : variantGrouped.entrySet()) {
            int systemQty = entry.getValue().size();
            ProductVariant variant = entry.getValue().get(0).getProductVariant();

            InventoryCheckDetail detail = InventoryCheckDetail.builder()
                    .inventoryCheck(check)
                    .productVariant(variant)
                    .systemQuantity(systemQty)
                    .actualQuantity(0)
                    .difference(-systemQty)
                    .build();
            inventoryCheckDetailRepository.save(detail);
            totalSystemQuantity += systemQty;
        }

        check.setSystemQuantity(totalSystemQuantity);
        check.setDifference(-totalSystemQuantity);
        inventoryCheckRepository.save(check);

        log.info("Created inventory check id: {} with {} variants", check.getId(), variantGrouped.size());
        return getInventoryCheckDetailById(check.getId());
    }

    @Override
    public PageResponse<InventoryCheckResponse> getAllInventoryChecks(InventoryCheckFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<InventoryCheckResponse> page = this.getAllWithFilterAndSearch(request, pageable);
        return PageResponse.fromPage(page);
    }

    @Override
    public InventoryCheckResponse getInventoryCheckDetailById(Long id) {
        InventoryCheck check = getInventoryCheckById(id);
        InventoryCheckResponse response = toResponse(check);

        List<InventoryCheckDetailResponse> details = inventoryCheckMyBatisMapper.findDetailsByCheckId(id);
        response.setDetails(details);

        return response;
    }

    @Override
    @Transactional
    public InventoryCheckResponse updateInventoryCheck(Long id, InventoryCheckRequest request) {
        InventoryCheck check = getInventoryCheckById(id);
        validateInProgressStatus(check);

        if (StringUtils.hasText(request.getCode())
                && inventoryCheckRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        if (StringUtils.hasText(request.getCode())) {
            check.setCode(request.getCode());
        }
        if (StringUtils.hasText(request.getCheckType())) {
            check.setCheckType(request.getCheckType());
        }
        if (request.getNotes() != null) {
            check.setNotes(request.getNotes());
        }

        inventoryCheckRepository.save(check);
        log.info("Updated inventory check id: {}", id);
        return getInventoryCheckDetailById(id);
    }

    @Override
    @Transactional
    public InventoryCheckResponse updateDetailActualQuantity(Long checkId, Long detailId,
            InventoryCheckDetailUpdateRequest request) {
        InventoryCheck check = getInventoryCheckById(checkId);
        validateInProgressStatus(check);

        InventoryCheckDetail detail = inventoryCheckDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_DETAIL_NOT_FOUND));

        if (!detail.getInventoryCheck().getId().equals(checkId)) {
            throw new AppException(ErrorCode.INVENTORY_CHECK_DETAIL_NOT_FOUND);
        }

        detail.setActualQuantity(request.getActualQuantity());
        detail.setDifference(request.getActualQuantity() - detail.getSystemQuantity());
        if (StringUtils.hasText(request.getReason())) {
            detail.setReason(request.getReason());
        }
        inventoryCheckDetailRepository.save(detail);

        // Recalculate totals
        recalculateTotals(check);

        log.info("Updated detail {} actual quantity to {} for check {}", detailId, request.getActualQuantity(),
                checkId);
        return getInventoryCheckDetailById(checkId);
    }

    @Override
    @Transactional
    public InventoryCheckResponse batchUpdateDetails(Long checkId, InventoryCheckBatchUpdateRequest request) {
        InventoryCheck check = getInventoryCheckById(checkId);
        validateInProgressStatus(check);

        for (InventoryCheckBatchUpdateRequest.BatchItem item : request.getItems()) {
            InventoryCheckDetail detail = inventoryCheckDetailRepository.findById(item.getDetailId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_DETAIL_NOT_FOUND));

            if (!detail.getInventoryCheck().getId().equals(checkId)) {
                throw new AppException(ErrorCode.INVENTORY_CHECK_DETAIL_NOT_FOUND);
            }

            detail.setActualQuantity(item.getActualQuantity());
            detail.setDifference(item.getActualQuantity() - detail.getSystemQuantity());
            if (StringUtils.hasText(item.getReason())) {
                detail.setReason(item.getReason());
            }
            inventoryCheckDetailRepository.save(detail);
        }

        // Recalculate totals
        recalculateTotals(check);

        log.info("Batch updated {} details for check {}", request.getItems().size(), checkId);
        return getInventoryCheckDetailById(checkId);
    }

    @Override
    @Transactional
    public void completeInventoryCheck(Long id) {
        InventoryCheck check = getInventoryCheckById(id);
        validateInProgressStatus(check);

        check.setStatus(InventoryCheckStatus.COMPLETED);
        inventoryCheckRepository.save(check);
        log.info("Completed inventory check id: {}, difference: {}", id, check.getDifference());
    }

    @Override
    @Transactional
    public void cancelInventoryCheck(Long id) {
        InventoryCheck check = getInventoryCheckById(id);
        validateInProgressStatus(check);

        check.setStatus(InventoryCheckStatus.CANCELLED);
        inventoryCheckRepository.save(check);
        log.info("Cancelled inventory check id: {}", id);
    }

    // ======================== Private methods ========================

    private InventoryCheck getInventoryCheckById(Long id) {
        return inventoryCheckRepository.findWithGraphById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
    }

    private void validateInProgressStatus(InventoryCheck check) {
        if (check.getStatus() != InventoryCheckStatus.IN_PROGRESS) {
            throw new AppException(ErrorCode.INVENTORY_CHECK_NOT_IN_PROGRESS);
        }
    }

    private void recalculateTotals(InventoryCheck check) {
        List<InventoryCheckDetail> allDetails = inventoryCheckDetailRepository.findByInventoryCheckId(check.getId());
        int totalActual = allDetails.stream().mapToInt(InventoryCheckDetail::getActualQuantity).sum();
        int totalSystem = allDetails.stream().mapToInt(InventoryCheckDetail::getSystemQuantity).sum();

        check.setActualQuantity(totalActual);
        check.setSystemQuantity(totalSystem);
        check.setDifference(totalActual - totalSystem);
        inventoryCheckRepository.save(check);
    }

    private InventoryCheckResponse toResponse(InventoryCheck check) {
        return InventoryCheckResponse.builder()
                .id(check.getId())
                .code(check.getCode())
                .warehouseId(check.getWarehouse().getId())
                .warehouseName(check.getWarehouse().getName())
                .checkDate(check.getCheckDate())
                .checkType(check.getCheckType())
                .systemQuantity(check.getSystemQuantity())
                .actualQuantity(check.getActualQuantity())
                .difference(check.getDifference())
                .status(check.getStatus())
                .notes(check.getNotes())
                .createdAt(check.getCreatedAt())
                .updatedAt(check.getUpdatedAt())
                .build();
    }

    private Page<InventoryCheckResponse> getAllWithFilterAndSearch(InventoryCheckFilterRequest request,
            Pageable pageable) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), InventoryCheckSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<InventoryCheckResponse> content = inventoryCheckMyBatisMapper.findAllWithFilterAndSearch(
                request.getWarehouseId(),
                request.getCheckType(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = inventoryCheckMyBatisMapper.countWithFilterAndSearch(
                request.getWarehouseId(),
                request.getCheckType(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate());

        return new PageImpl<>(content, pageable, total);
    }

}
