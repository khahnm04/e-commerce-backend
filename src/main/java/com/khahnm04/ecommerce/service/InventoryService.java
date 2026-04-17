package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.entity.ProductVariant;
import java.util.Collection;
import java.util.Map;

public interface InventoryService {

    void checkAvailability(Long productVariantId, Integer quantity);

    void initializeStock(ProductVariant productVariant, Integer initialQuantity);

    void updateStock(Long productVariantId, Integer quantity);

    Integer getQuantityByVariantId(Long productVariantId);

    Map<Long, Integer> getQuantitiesByVariantIds(Collection<Long> productVariantIds);

    void deleteInventoryByVariantId(Long productVariantId);

    void deleteInventoryByVariantIds(Collection<Long> productVariantIds);

}
