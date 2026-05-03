package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.entity.Inventory;
import com.khahnm04.ecommerce.entity.ProductVariant;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.InventoryRepository;
import com.khahnm04.ecommerce.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public void checkAvailability(Long productVariantId, Integer requestedQty) {
        Inventory inventory = inventoryRepository.findByProductVariantId(productVariantId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));

        if (inventory.getQuantity() == null || inventory.getQuantity() < requestedQty) {
            log.warn("Insufficient stock for variant ID {}: current={}, requested={}", productVariantId,
                    inventory.getQuantity(), requestedQty);
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }

    @Override
    @Transactional
    public void initializeStock(ProductVariant productVariant, Integer initialQuantity) {
        if (inventoryRepository.findByProductVariantId(productVariant.getId()).isPresent()) {
            log.info("Inventory already exists for variant ID: {}", productVariant.getId());
            return;
        }

        Inventory inventory = Inventory.builder()
                .productVariant(productVariant)
                .quantity(initialQuantity != null ? initialQuantity : 0)
                .build();

        inventoryRepository.save(inventory);
        log.info("Initialized inventory for variant ID: {} with quantity: {}", productVariant.getId(),
                inventory.getQuantity());
    }

    @Override
    @Transactional
    public void initializeStocks(Map<ProductVariant, Integer> variantQuantities) {
        if (variantQuantities == null || variantQuantities.isEmpty()) {
            return;
        }

        Set<Long> variantIds = variantQuantities.keySet().stream()
                .map(ProductVariant::getId)
                .collect(Collectors.toSet());
        Set<Long> existingIds = inventoryRepository.findAllByProductVariantIdIn(variantIds)
                .stream().map(inv -> inv.getProductVariant().getId()).collect(Collectors.toSet());

        List<Inventory> inventories = variantQuantities.entrySet().stream()
                .filter(entry -> !existingIds.contains(entry.getKey().getId()))
                .map(entry -> Inventory.builder()
                        .productVariant(entry.getKey())
                        .quantity(entry.getValue() != null ? entry.getValue() : 0)
                        .build())
                .collect(Collectors.toList());

        if (!inventories.isEmpty()) {
            inventoryRepository.saveAll(inventories);
            log.info("Initialized inventory for {} variants", inventories.size());
        }
    }

    @Override
    @Transactional
    public void updateStock(Long productVariantId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductVariantId(productVariantId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));

        inventory.setQuantity(quantity != null ? quantity : 0);
        inventoryRepository.save(inventory);
        log.info("Updated inventory for variant ID: {} to quantity: {}", productVariantId, inventory.getQuantity());
    }

    @Override
    public Integer getQuantityByVariantId(Long productVariantId) {
        return inventoryRepository.findByProductVariantId(productVariantId)
                .map(Inventory::getQuantity)
                .orElse(0);
    }

    @Override
    public Map<Long, Integer> getQuantitiesByVariantIds(Collection<Long> productVariantIds) {
        if (productVariantIds == null || productVariantIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return inventoryRepository.findAllByProductVariantIdIn(productVariantIds)
                .stream()
                .collect(Collectors.toMap(
                        inv -> inv.getProductVariant().getId(),
                        inv -> inv.getQuantity() != null ? inv.getQuantity() : 0));
    }

    @Override
    @Transactional
    public void deleteInventoryByVariantId(Long productVariantId) {
        inventoryRepository.deleteByProductVariantId(productVariantId);
        log.info("Deleted inventory for product variant ID: {}", productVariantId);
    }

    @Override
    @Transactional
    public void deleteInventoryByVariantIds(Collection<Long> productVariantIds) {
        if (productVariantIds != null && !productVariantIds.isEmpty()) {
            inventoryRepository.deleteByProductVariantIdIn(productVariantIds);
            log.info("Deleted inventories for product variant IDs: {}", productVariantIds);
        }
    }

}
