package com.khahnm04.ecommerce.modules.branch.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BranchSortField implements SortableField {

    CREATED_AT("createdAt", "b.created_at", true),
    UPDATED_AT("updatedAt", "updated_at", false),
    CODE("code", "b.code", false),
    NAME("name", "b.name", false),
    CITY("city", "b.city", false),
    STATUS("status", "b.status", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
