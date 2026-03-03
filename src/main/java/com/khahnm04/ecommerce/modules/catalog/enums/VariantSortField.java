package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VariantSortField implements SortableField {

    CREATED_AT("createdAt", "v.created_at", true),
    UPDATED_AT("updatedAt", "v.updated_at", false),
    NAME("name", "v.name", false),
    CODE("code", "v.code", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
