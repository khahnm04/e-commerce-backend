package com.khahnm04.ecommerce.modules.inventory.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupplierSortField implements SortableField {

    CREATED_AT("createdAt", "s.created_at", true),
    UPDATED_AT("updatedAt", "s.updated_at", false),
    CODE("code", "s.code", false),
    NAME("name", "s.name", false),
    STATUS("status", "s.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
