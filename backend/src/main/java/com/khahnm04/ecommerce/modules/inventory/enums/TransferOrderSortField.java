package com.khahnm04.ecommerce.modules.inventory.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransferOrderSortField implements SortableField {

    CREATED_AT("createdAt", "t.created_at", true),
    UPDATED_AT("updatedAt", "t.updated_at", false),
    CODE("code", "t.code", false),
    TRANSFER_DATE("transferDate", "t.transfer_date", false),
    TOTAL_QUANTITY("totalQuantity", "t.total_quantity", false),
    STATUS("status", "t.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
