package com.khahnm04.ecommerce.modules.catalog.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategorySortField implements SortableField {

    CREATED_AT("createdAt", "c.created_at", true),
    UPDATED_AT("updatedAt", "c.updated_at", false),
    NAME("name", "c.name", false),
    SLUG("slug", "c.slug", false),
    POSITION("position", "c.position", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
