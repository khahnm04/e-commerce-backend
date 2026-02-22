package com.khahnm04.ecommerce.modules.inventory.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SerialSortField implements SortableField {

    CREATED_AT("createdAt", "sn.created_at", true),
    UPDATED_AT("updatedAt", "sn.updated_at", false),
    SERIAL_CODE("serialCode", "sn.serial_code", false),
    STATUS("status", "sn.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
