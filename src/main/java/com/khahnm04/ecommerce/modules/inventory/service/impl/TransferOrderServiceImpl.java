package com.khahnm04.ecommerce.modules.inventory.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferDetailRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferOrderFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.TransferOrderRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferDetailResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.TransferOrderResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.*;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialMovementType;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.TransferOrderSortField;
import com.khahnm04.ecommerce.modules.inventory.enums.TransferOrderStatus;
import com.khahnm04.ecommerce.modules.inventory.mapper.TransferOrderMapper;
import com.khahnm04.ecommerce.modules.inventory.mapper.mybatis.TransferOrderMyBatisMapper;
import com.khahnm04.ecommerce.modules.inventory.repository.*;
import com.khahnm04.ecommerce.modules.inventory.service.TransferOrderService;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderServiceImpl implements TransferOrderService {

    private final TransferOrderRepository transferOrderRepository;
    private final TransferDetailRepository transferDetailRepository;
    private final SerialNumberRepository serialNumberRepository;
    private final SerialMovementLogRepository serialMovementLogRepository;
    private final WarehouseRepository warehouseRepository;
    private final TransferOrderMapper transferOrderMapper;
    private final TransferOrderMyBatisMapper transferOrderMyBatisMapper;

    @Override
    @Transactional
    public TransferOrderResponse createTransferOrder(TransferOrderRequest request) {
        if (transferOrderRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_EXISTED);
        }

        if (request.getFromWarehouseId().equals(request.getToWarehouseId())) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_SAME_WAREHOUSE);
        }

        Warehouse fromWarehouse = warehouseRepository.findById(request.getFromWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        Warehouse toWarehouse = warehouseRepository.findById(request.getToWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        TransferOrder transferOrder = TransferOrder.builder()
                .code(request.getCode())
                .fromWarehouse(fromWarehouse)
                .toWarehouse(toWarehouse)
                .transferDate(LocalDateTime.now())
                .status(TransferOrderStatus.PENDING)
                .shippingMethod(request.getShippingMethod())
                .notes(request.getNotes())
                .build();

        TransferOrder saved = transferOrderRepository.save(transferOrder);
        log.info("Created transfer order id: {}", saved.getId());
        return transferOrderMapper.toTransferOrderResponse(saved);
    }

    @Override
    public PageResponse<TransferOrderResponse> getAllTransferOrders(TransferOrderFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<TransferOrderResponse> page = this.getAllWithFilterAndSearch(request, pageable);
        return PageResponse.fromPage(page);
    }

    @Override
    public TransferOrderResponse getTransferOrderDetailById(Long id) {
        TransferOrder transferOrder = getTransferOrderById(id);
        TransferOrderResponse response = transferOrderMapper.toTransferOrderResponse(transferOrder);

        List<TransferDetailResponse> details = transferOrderMyBatisMapper.findDetailsByTransferOrderId(id);
        response.setDetails(details);

        return response;
    }

    @Override
    @Transactional
    public TransferOrderResponse updateTransferOrder(Long id, TransferOrderRequest request) {
        TransferOrder transferOrder = getTransferOrderById(id);
        validatePendingStatus(transferOrder);

        if (StringUtils.hasText(request.getCode())
                && transferOrderRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        if (request.getFromWarehouseId() != null && request.getToWarehouseId() != null
                && request.getFromWarehouseId().equals(request.getToWarehouseId())) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_SAME_WAREHOUSE);
        }

        if (request.getFromWarehouseId() != null) {
            Warehouse fromWarehouse = warehouseRepository.findById(request.getFromWarehouseId())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));
            transferOrder.setFromWarehouse(fromWarehouse);
        }
        if (request.getToWarehouseId() != null) {
            Warehouse toWarehouse = warehouseRepository.findById(request.getToWarehouseId())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));
            transferOrder.setToWarehouse(toWarehouse);
        }
        if (StringUtils.hasText(request.getCode())) {
            transferOrder.setCode(request.getCode());
        }
        if (request.getShippingMethod() != null) {
            transferOrder.setShippingMethod(request.getShippingMethod());
        }
        if (request.getNotes() != null) {
            transferOrder.setNotes(request.getNotes());
        }

        TransferOrder saved = transferOrderRepository.save(transferOrder);
        log.info("Updated transfer order id: {}", saved.getId());
        return transferOrderMapper.toTransferOrderResponse(saved);
    }

    @Override
    @Transactional
    public TransferOrderResponse addItemToTransferOrder(Long id, TransferDetailRequest request) {
        TransferOrder transferOrder = getTransferOrderById(id);
        validatePendingStatus(transferOrder);

        SerialNumber serialNumber = serialNumberRepository.findById(request.getSerialNumberId())
                .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NOT_FOUND));

        // Validate serial is IN_STOCK at the from_warehouse
        if (serialNumber.getStatus() != SerialNumberStatus.IN_STOCK) {
            throw new AppException(ErrorCode.TRANSFER_SERIAL_NOT_IN_STOCK);
        }
        if (serialNumber.getWarehouse() == null
                || !serialNumber.getWarehouse().getId().equals(transferOrder.getFromWarehouse().getId())) {
            throw new AppException(ErrorCode.TRANSFER_SERIAL_WRONG_WAREHOUSE);
        }

        TransferDetail detail = TransferDetail.builder()
                .transferOrder(transferOrder)
                .serialNumber(serialNumber)
                .build();
        transferDetailRepository.save(detail);

        // Update total quantity
        transferOrder.setTotalQuantity(transferOrder.getTotalQuantity() + 1);
        transferOrderRepository.save(transferOrder);

        log.info("Added serial {} to transfer order {}", serialNumber.getSerialCode(), id);
        return getTransferOrderDetailById(id);
    }

    @Override
    @Transactional
    public void removeItemFromTransferOrder(Long transferOrderId, Long detailId) {
        TransferOrder transferOrder = getTransferOrderById(transferOrderId);
        validatePendingStatus(transferOrder);

        TransferDetail detail = transferDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSFER_DETAIL_NOT_FOUND));

        if (!detail.getTransferOrder().getId().equals(transferOrderId)) {
            throw new AppException(ErrorCode.TRANSFER_DETAIL_NOT_FOUND);
        }

        // Update total quantity
        transferOrder.setTotalQuantity(transferOrder.getTotalQuantity() - 1);
        transferOrderRepository.save(transferOrder);

        transferDetailRepository.delete(detail);
        log.info("Removed detail {} from transfer order {}", detailId, transferOrderId);
    }

    @Override
    @Transactional
    public void startShipping(Long id) {
        TransferOrder transferOrder = getTransferOrderById(id);
        validatePendingStatus(transferOrder);

        if (transferOrder.getTotalQuantity() == 0) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_EMPTY);
        }

        // Mark serials as TRANSFERRED
        List<TransferDetail> details = transferDetailRepository.findByTransferOrderId(id);
        for (TransferDetail detail : details) {
            SerialNumber serial = detail.getSerialNumber();
            serial.setStatus(SerialNumberStatus.TRANSFERRED);
            serialNumberRepository.save(serial);
        }

        transferOrder.setStatus(TransferOrderStatus.IN_TRANSIT);
        transferOrderRepository.save(transferOrder);
        log.info("Started shipping for transfer order id: {}", id);
    }

    @Override
    @Transactional
    public void completeTransferOrder(Long id) {
        TransferOrder transferOrder = getTransferOrderById(id);

        if (transferOrder.getStatus() != TransferOrderStatus.IN_TRANSIT) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_NOT_IN_TRANSIT);
        }

        List<TransferDetail> details = transferDetailRepository.findByTransferOrderId(id);
        for (TransferDetail detail : details) {
            SerialNumber serial = detail.getSerialNumber();

            // Move serial to destination warehouse
            Warehouse fromWarehouse = serial.getWarehouse();
            serial.setWarehouse(transferOrder.getToWarehouse());
            serial.setStatus(SerialNumberStatus.IN_STOCK);
            serialNumberRepository.save(serial);

            // Create movement log
            SerialMovementLog movementLog = SerialMovementLog.builder()
                    .serialNumber(serial)
                    .movementType(SerialMovementType.TRANSFER)
                    .fromWarehouse(fromWarehouse)
                    .toWarehouse(transferOrder.getToWarehouse())
                    .referenceType("TRANSFER_ORDER")
                    .referenceId(transferOrder.getId())
                    .movementDate(LocalDateTime.now())
                    .notes("Transfer: " + transferOrder.getFromWarehouse().getName()
                            + " → " + transferOrder.getToWarehouse().getName())
                    .build();
            serialMovementLogRepository.save(movementLog);
        }

        transferOrder.setStatus(TransferOrderStatus.COMPLETED);
        transferOrder.setReceivedDate(LocalDateTime.now());
        transferOrderRepository.save(transferOrder);
        log.info("Completed transfer order id: {}", id);
    }

    @Override
    @Transactional
    public void cancelTransferOrder(Long id) {
        TransferOrder transferOrder = getTransferOrderById(id);

        if (transferOrder.getStatus() != TransferOrderStatus.PENDING
                && transferOrder.getStatus() != TransferOrderStatus.IN_TRANSIT) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_CANNOT_CANCEL);
        }

        // Rollback serials to IN_STOCK if they were TRANSFERRED
        List<TransferDetail> details = transferDetailRepository.findByTransferOrderId(id);
        for (TransferDetail detail : details) {
            SerialNumber serial = detail.getSerialNumber();
            if (serial.getStatus() == SerialNumberStatus.TRANSFERRED) {
                serial.setStatus(SerialNumberStatus.IN_STOCK);
                serialNumberRepository.save(serial);
            }
        }

        transferOrder.setStatus(TransferOrderStatus.CANCELLED);
        transferOrderRepository.save(transferOrder);
        log.info("Cancelled transfer order id: {}", id);
    }

    // ======================== Private methods ========================

    private TransferOrder getTransferOrderById(Long id) {
        return transferOrderRepository.findWithGraphById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSFER_ORDER_NOT_FOUND));
    }

    private void validatePendingStatus(TransferOrder transferOrder) {
        if (transferOrder.getStatus() != TransferOrderStatus.PENDING) {
            throw new AppException(ErrorCode.TRANSFER_ORDER_NOT_PENDING);
        }
    }

    private Page<TransferOrderResponse> getAllWithFilterAndSearch(TransferOrderFilterRequest request,
            Pageable pageable) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), TransferOrderSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<TransferOrderResponse> content = transferOrderMyBatisMapper.findAllWithFilterAndSearch(
                request.getFromWarehouseId(),
                request.getToWarehouseId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = transferOrderMyBatisMapper.countWithFilterAndSearch(
                request.getFromWarehouseId(),
                request.getToWarehouseId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate());

        return new PageImpl<>(content, pageable, total);
    }

}
