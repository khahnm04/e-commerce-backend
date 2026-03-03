package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BrandSortField implements SortableField {

    CREATED_AT("createdAt", "b.created_at", true),
    UPDATED_AT("updatedAt", "b.updated_at", false),
    NAME("name", "b.name", false),
    SLUG("slug", "b.slug", false),
    COUNTRY("country", "b.country", false),
    STATUS("status", "b.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
