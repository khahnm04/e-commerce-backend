package com.khahnm04.ecommerce.modules.auth.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleSortField implements SortableField {

    CREATED_AT("createdAt", "r.created_at", true),
    UPDATED_AT("updatedAt", "r.updated_at", false),
    CODE("code", "r.code", false),
    NAME("name", "r.name", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
