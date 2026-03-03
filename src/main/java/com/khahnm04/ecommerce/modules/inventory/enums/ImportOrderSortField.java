package com.khahnm04.ecommerce.modules.inventory.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImportOrderSortField implements SortableField {

    CREATED_AT("createdAt", "io.created_at", true),
    UPDATED_AT("updatedAt", "io.updated_at", false),
    CODE("code", "io.code", false),
    IMPORT_DATE("importDate", "io.import_date", false),
    TOTAL_AMOUNT("totalAmount", "io.total_amount", false),
    STATUS("status", "io.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
