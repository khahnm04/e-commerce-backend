package com.khahnm04.ecommerce.modules.inventory.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import com.khahnm04.ecommerce.modules.catalog.repository.ProductVariantRepository;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportDetailRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.ImportOrderRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportDetailResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.ImportOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.*;
import com.khahnm04.ecommerce.modules.inventory.enums.ImportOrderSortField;
import com.khahnm04.ecommerce.modules.inventory.enums.ImportOrderStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialMovementType;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import com.khahnm04.ecommerce.modules.inventory.mapper.ImportOrderMapper;
import com.khahnm04.ecommerce.modules.inventory.mapper.mybatis.ImportOrderMyBatisMapper;
import com.khahnm04.ecommerce.modules.inventory.repository.*;
import com.khahnm04.ecommerce.modules.inventory.service.ImportOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportOrderServiceImpl implements ImportOrderService {

    private final ImportOrderRepository importOrderRepository;
    private final ImportDetailRepository importDetailRepository;
    private final SerialNumberRepository serialNumberRepository;
    private final SerialMovementLogRepository serialMovementLogRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ImportOrderMapper importOrderMapper;
    private final ImportOrderMyBatisMapper importOrderMyBatisMapper;

    @Override
    @Transactional
    public ImportOrderResponse createImportOrder(ImportOrderRequest request) {
        if (importOrderRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.IMPORT_ORDER_EXISTED);
        }

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));

        ImportOrder importOrder = ImportOrder.builder()
                .code(request.getCode())
                .warehouse(warehouse)
                .supplier(supplier)
                .importDate(LocalDateTime.now())
                .status(ImportOrderStatus.PENDING)
                .notes(request.getNotes())
                .build();

        ImportOrder saved = importOrderRepository.save(importOrder);
        log.info("Created import order id: {}", saved.getId());
        return importOrderMapper.toImportOrderResponse(saved);
    }

    @Override
    public PageResponse<ImportOrderResponse> getAllImportOrders(ImportOrderFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<ImportOrderResponse> page = this.getAllWithFilterAndSearch(request, pageable);
        return PageResponse.fromPage(page);
    }

    @Override
    public ImportOrderResponse getImportOrderDetailById(Long id) {
        ImportOrder importOrder = getImportOrderById(id);
        ImportOrderResponse response = importOrderMapper.toImportOrderResponse(importOrder);

        List<ImportDetailResponse> details = importOrderMyBatisMapper.findDetailsByImportOrderId(id);
        response.setDetails(details);

        return response;
    }

    @Override
    @Transactional
    public ImportOrderResponse updateImportOrder(Long id, ImportOrderRequest request) {
        ImportOrder importOrder = getImportOrderById(id);
        validatePendingStatus(importOrder);

        if (StringUtils.hasText(request.getCode())
                && importOrderRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));
            importOrder.setWarehouse(warehouse);
        }
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
            importOrder.setSupplier(supplier);
        }
        if (StringUtils.hasText(request.getCode())) {
            importOrder.setCode(request.getCode());
        }
        if (request.getNotes() != null) {
            importOrder.setNotes(request.getNotes());
        }

        ImportOrder saved = importOrderRepository.save(importOrder);
        log.info("Updated import order id: {}", saved.getId());
        return importOrderMapper.toImportOrderResponse(saved);
    }

    @Override
    @Transactional
    public ImportOrderResponse addItemToImportOrder(Long id, ImportDetailRequest request) {
        ImportOrder importOrder = getImportOrderById(id);
        validatePendingStatus(importOrder);

        // Check if serial code already exists
        if (serialNumberRepository.existsBySerialCode(request.getSerialCode())) {
            throw new AppException(ErrorCode.SERIAL_ALREADY_ALLOCATED);
        }

        // Get product variant
        ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        // Create serial number (IN_STOCK in the import warehouse)
        SerialNumber serialNumber = SerialNumber.builder()
                .serialCode(request.getSerialCode())
                .productVariant(productVariant)
                .warehouse(importOrder.getWarehouse())
                .status(SerialNumberStatus.IN_STOCK)
                .purchasePrice(request.getUnitPrice())
                .build();
        serialNumber = serialNumberRepository.save(serialNumber);

        // Create import detail
        ImportDetail detail = ImportDetail.builder()
                .importOrder(importOrder)
                .serialNumber(serialNumber)
                .unitPrice(request.getUnitPrice())
                .build();
        importDetailRepository.save(detail);

        // Update totals
        importOrder.setTotalQuantity(importOrder.getTotalQuantity() + 1);
        importOrder.setTotalAmount(importOrder.getTotalAmount().add(request.getUnitPrice()));
        importOrderRepository.save(importOrder);

        log.info("Added serial {} to import order {}", request.getSerialCode(), id);
        return getImportOrderDetailById(id);
    }

    @Override
    @Transactional
    public void removeItemFromImportOrder(Long importOrderId, Long detailId) {
        ImportOrder importOrder = getImportOrderById(importOrderId);
        validatePendingStatus(importOrder);

        ImportDetail detail = importDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.IMPORT_DETAIL_NOT_FOUND));

        if (!detail.getImportOrder().getId().equals(importOrderId)) {
            throw new AppException(ErrorCode.IMPORT_DETAIL_NOT_FOUND);
        }

        // Remove serial number
        serialNumberRepository.delete(detail.getSerialNumber());

        // Update totals
        importOrder.setTotalQuantity(importOrder.getTotalQuantity() - 1);
        importOrder.setTotalAmount(importOrder.getTotalAmount().subtract(detail.getUnitPrice()));
        importOrderRepository.save(importOrder);

        // Remove detail
        importDetailRepository.delete(detail);

        log.info("Removed detail {} from import order {}", detailId, importOrderId);
    }

    @Override
    @Transactional
    public void completeImportOrder(Long id) {
        ImportOrder importOrder = getImportOrderById(id);
        validatePendingStatus(importOrder);

        if (importOrder.getTotalQuantity() == 0) {
            throw new AppException(ErrorCode.IMPORT_ORDER_EMPTY);
        }

        // Create movement logs for all serials
        List<ImportDetail> details = importDetailRepository.findByImportOrderId(id);
        for (ImportDetail detail : details) {
            SerialMovementLog movementLog = SerialMovementLog.builder()
                    .serialNumber(detail.getSerialNumber())
                    .movementType(SerialMovementType.IMPORT)
                    .toWarehouse(importOrder.getWarehouse())
                    .referenceType("IMPORT_ORDER")
                    .referenceId(importOrder.getId())
                    .movementDate(LocalDateTime.now())
                    .notes("Import from supplier: " + importOrder.getSupplier().getName())
                    .build();
            serialMovementLogRepository.save(movementLog);
        }

        importOrder.setStatus(ImportOrderStatus.COMPLETED);
        importOrderRepository.save(importOrder);
        log.info("Completed import order id: {}", id);
    }

    @Override
    @Transactional
    public void cancelImportOrder(Long id) {
        ImportOrder importOrder = getImportOrderById(id);
        validatePendingStatus(importOrder);

        // Delete all serials created for this import
        List<ImportDetail> details = importDetailRepository.findByImportOrderId(id);
        for (ImportDetail detail : details) {
            serialNumberRepository.delete(detail.getSerialNumber());
        }

        // Delete all details
        importDetailRepository.deleteByImportOrderId(id);

        // Reset totals and cancel
        importOrder.setTotalQuantity(0);
        importOrder.setTotalAmount(BigDecimal.ZERO);
        importOrder.setStatus(ImportOrderStatus.CANCELLED);
        importOrderRepository.save(importOrder);
        log.info("Cancelled import order id: {}", id);
    }

    // ======================== Private methods ========================

    private ImportOrder getImportOrderById(Long id) {
        return importOrderRepository.findWithGraphById(id)
                .orElseThrow(() -> new AppException(ErrorCode.IMPORT_ORDER_NOT_FOUND));
    }

    private void validatePendingStatus(ImportOrder importOrder) {
        if (importOrder.getStatus() != ImportOrderStatus.PENDING) {
            throw new AppException(ErrorCode.IMPORT_ORDER_NOT_PENDING);
        }
    }

    private Page<ImportOrderResponse> getAllWithFilterAndSearch(ImportOrderFilterRequest request, Pageable pageable) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), ImportOrderSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<ImportOrderResponse> content = importOrderMyBatisMapper.findAllWithFilterAndSearch(
                request.getWarehouseId(),
                request.getSupplierId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = importOrderMyBatisMapper.countWithFilterAndSearch(
                request.getWarehouseId(),
                request.getSupplierId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate());

        return new PageImpl<>(content, pageable, total);
    }

}
