package com.khahnm04.ecommerce.modules.order.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderSortField implements SortableField {

    CREATED_AT("createdAt", "o.created_at", true),
    UPDATED_AT("updatedAt", "o.updated_at", false),
    ORDER_CODE("orderCode", "o.order_code", false),
    TOTAL_AMOUNT("totalAmount", "o.total_amount", false),
    ORDER_STATUS("orderStatus", "o.order_status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
