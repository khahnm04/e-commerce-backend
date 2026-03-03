package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VariantValueSortField implements SortableField {

    CREATED_AT("createdAt", "vv.created_at", true),
    VALUE("value", "vv.value", false),
    CODE("code", "vv.code", false),
    DISPLAY_ORDER("displayOrder", "vv.display_order", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
