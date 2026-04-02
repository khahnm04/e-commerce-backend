package com.khahnm04.ecommerce.service.inventory;

import com.khahnm04.ecommerce.entity.product.ProductVariant;
import java.util.Collection;
import java.util.Map;

public interface InventoryService {

    void checkAvailability(Long productVariantId, Integer quantity);

    void initializeStock(ProductVariant productVariant, Integer initialQuantity);

    void updateStock(Long productVariantId, Integer quantity);

    Integer getQuantityByVariantId(Long productVariantId);

    Map<Long, Integer> getQuantitiesByVariantIds(Collection<Long> productVariantIds);

}
