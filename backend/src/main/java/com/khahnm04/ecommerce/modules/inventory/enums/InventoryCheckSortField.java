package com.khahnm04.ecommerce.modules.inventory.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryCheckSortField implements SortableField {

    CREATED_AT("createdAt", "ic.created_at", true),
    UPDATED_AT("updatedAt", "ic.updated_at", false),
    CODE("code", "ic.code", false),
    CHECK_DATE("checkDate", "ic.check_date", false),
    STATUS("status", "ic.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
