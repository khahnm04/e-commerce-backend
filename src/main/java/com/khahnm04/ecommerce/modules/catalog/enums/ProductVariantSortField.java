package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductVariantSortField implements SortableField {

    CREATED_AT("createdAt", "pv.created_at", true),
    UPDATED_AT("updatedAt", "pv.updated_at", false),
    NAME("name", "pv.name", false),
    SKU("sku", "pv.sku", false),
    PRICE("price", "pv.price", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
