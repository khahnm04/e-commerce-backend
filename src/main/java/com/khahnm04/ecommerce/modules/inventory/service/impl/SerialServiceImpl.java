package com.khahnm04.ecommerce.modules.inventory.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialFilterRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialUpdateNotesRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.request.SerialUpdateStatusRequest;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialMovementLogResponse;
import com.khahnm04.ecommerce.modules.inventory.dto.response.SerialNumberResponse;
import com.khahnm04.ecommerce.modules.inventory.entity.SerialNumber;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialSortField;
import com.khahnm04.ecommerce.modules.inventory.mapper.SerialMapper;
import com.khahnm04.ecommerce.modules.inventory.mapper.mybatis.SerialMyBatisMapper;
import com.khahnm04.ecommerce.modules.inventory.repository.SerialNumberRepository;
import com.khahnm04.ecommerce.modules.inventory.service.SerialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SerialServiceImpl implements SerialService {

    private final SerialNumberRepository serialNumberRepository;
    private final SerialMapper serialMapper;
    private final SerialMyBatisMapper serialMyBatisMapper;

    @Override
    public PageResponse<SerialNumberResponse> getAllSerials(SerialFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<SerialNumberResponse> page = this.getAllWithFilterAndSearch(request, pageable);
        return PageResponse.fromPage(page);
    }

    @Override
    public SerialNumberResponse getSerialDetailById(Long id) {
        SerialNumber serialNumber = getSerialById(id);
        return serialMapper.toSerialNumberResponse(serialNumber);
    }

    @Override
    public SerialNumberResponse checkSerialByCode(String serialCode) {
        SerialNumber serialNumber = serialNumberRepository.findBySerialCode(serialCode)
                .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NOT_FOUND));
        return serialMapper.toSerialNumberResponse(serialNumber);
    }

    @Override
    public List<SerialNumberResponse> getSerialsByWarehouse(Long warehouseId) {
        List<SerialNumber> serials = serialNumberRepository.findByWarehouseId(warehouseId);
        return serialMapper.toSerialNumberResponseList(serials);
    }

    @Override
    public List<SerialNumberResponse> getSerialsByVariant(Long productVariantId) {
        List<SerialNumber> serials = serialNumberRepository.findByProductVariantId(productVariantId);
        return serialMapper.toSerialNumberResponseList(serials);
    }

    @Override
    @Transactional
    public void updateSerialStatus(Long id, SerialUpdateStatusRequest request) {
        SerialNumber serialNumber = getSerialById(id);

        if (serialNumber.getStatus() == request.getStatus()) {
            return;
        }

        serialNumber.setStatus(request.getStatus());
        if (StringUtils.hasText(request.getNotes())) {
            serialNumber.setNotes(request.getNotes());
        }
        serialNumberRepository.save(serialNumber);
        log.info("Updated serial id {} status to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void updateSerialNotes(Long id, SerialUpdateNotesRequest request) {
        SerialNumber serialNumber = getSerialById(id);
        serialNumber.setNotes(request.getNotes());
        serialNumberRepository.save(serialNumber);
        log.info("Updated notes for serial id {}", id);
    }

    @Override
    public List<SerialMovementLogResponse> getSerialMovements(Long id) {
        // Verify serial exists
        getSerialById(id);
        return serialMyBatisMapper.findMovementsBySerialId(id);
    }

    @Override
    public List<SerialNumberResponse> getAvailableSerials(Long productVariantId, Long warehouseId) {
        List<SerialNumber> serials = serialNumberRepository.findAvailableSerials(
                SerialNumberStatus.IN_STOCK, warehouseId, productVariantId);
        return serialMapper.toSerialNumberResponseList(serials);
    }

    // ======================== Private methods ========================

    private SerialNumber getSerialById(Long id) {
        return serialNumberRepository.findWithGraphById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NOT_FOUND));
    }

    private Page<SerialNumberResponse> getAllWithFilterAndSearch(SerialFilterRequest request, Pageable pageable) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), SerialSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<SerialNumberResponse> content = serialMyBatisMapper.findAllWithFilterAndSearch(
                request.getProductVariantId(),
                request.getWarehouseId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = serialMyBatisMapper.countWithFilterAndSearch(
                request.getProductVariantId(),
                request.getWarehouseId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate());

        return new PageImpl<>(content, pageable, total);
    }

}
