package com.khahnm04.ecommerce.modules.inventory.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WarehouseSortField implements SortableField {

    CREATED_AT("createdAt", "w.created_at", true),
    UPDATED_AT("updatedAt", "w.updated_at", false),
    CODE("code", "w.code", false),
    NAME("name", "w.name", false),
    TYPE("type", "w.type", false),
    STATUS("status", "w.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
