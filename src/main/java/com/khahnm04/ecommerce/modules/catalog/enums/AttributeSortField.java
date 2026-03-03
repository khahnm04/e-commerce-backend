package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttributeSortField implements SortableField {

    CREATED_AT("createdAt", "a.created_at", true),
    UPDATED_AT("updatedAt", "a.updated_at", false),
    NAME("name", "a.name", false),
    CODE("code", "a.code", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
