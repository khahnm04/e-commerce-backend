package com.khahnm04.ecommerce.modules.user.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeSortField implements SortableField {

    CREATED_AT("createdAt", "e.created_at", true),
    UPDATED_AT("updatedAt", "updated_at", false),
    FULL_NAME("fullName", "u.full_name", false),
    CODE("code", "e.code", false),
    EMAIL("email", "u.email", false),
    STATUS("status", "u.status", false),
    BRANCH("branch", "b.name", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
