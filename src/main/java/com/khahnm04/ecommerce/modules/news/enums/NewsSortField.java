package com.khahnm04.ecommerce.modules.news.enums;

import com.khahnm04.ecommerce.common.util.SortableField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NewsSortField implements SortableField {

    CREATED_AT("createdAt", "n.created_at", true),
    UPDATED_AT("updatedAt", "n.updated_at", false),
    TITLE("title", "n.title", false),
    VIEW_COUNT("viewCount", "n.view_count", false),
    PUBLISHED_AT("publishedAt", "n.published_at", false);

    private final String fieldName;
    private final String sqlColumn;
    private final boolean isDefault;

    @Override
    public boolean isDefault() {
        return isDefault;
    }

}
