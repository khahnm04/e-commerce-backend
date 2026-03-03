package com.khahnm04.ecommerce.common.util;

/**
 * Interface cho các sortable field enums.
 * Mỗi module định nghĩa enum riêng implement interface này.
 */
public interface SortableField {

    /**
     * @return Tên field từ client (camelCase)
     */
    String getFieldName();

    /**
     * @return SQL column expression
     */
    String getSqlColumn();

    /**
     * @return true nếu đây là field mặc định để sort
     */
    boolean isDefault();

}
