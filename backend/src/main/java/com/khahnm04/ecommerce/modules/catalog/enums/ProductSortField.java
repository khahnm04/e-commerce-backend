package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSortField implements SortableField {

    CREATED_AT("createdAt", "p.created_at", true),
    UPDATED_AT("updatedAt", "p.updated_at", false),
    NAME("name", "p.name", false),
    SLUG("slug", "p.slug", false),
    PRICE("price", "p.price", false),
    PRODUCT_CODE("productCode", "p.product_code", false),
    STATUS("status", "p.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
