package com.khahnm04.ecommerce.common.util;

import org.springframework.data.domain.Sort;
import java.util.Arrays;

public class SortUtils {

    private static final String DEFAULT_SORT_DIRECTION = "DESC";

    public static Sort parseSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.unsorted();
        }

        String[] parts = sort.trim().split(",");
        String property = parts[0].trim();

        Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, property);
    }

    /**
     * Parse sort string cho MyBatis query sử dụng SortableField enum.
     *
     * @param sort           Sort string dạng "fieldName,asc" hoặc "fieldName,desc"
     * @param sortFieldClass Class của enum implement SortableField
     * @param <T>            Enum type
     * @return String[] với [0] = SQL column expression, [1] = "ASC" hoặc "DESC"
     */
    public static <T extends Enum<T> & SortableField> String[] parseSortForMyBatis(
            String sort, Class<T> sortFieldClass) {

        T[] enumConstants = sortFieldClass.getEnumConstants();

        // Tìm default field
        String defaultColumn = Arrays.stream(enumConstants)
                .filter(SortableField::isDefault)
                .findFirst()
                .map(SortableField::getSqlColumn)
                .orElse(enumConstants[0].getSqlColumn());

        if (sort == null || sort.trim().isEmpty()) {
            return new String[] { defaultColumn, DEFAULT_SORT_DIRECTION };
        }

        String[] parts = sort.trim().split(",");
        String fieldName = parts[0].trim();

        // Tìm field matching
        String sqlColumn = Arrays.stream(enumConstants)
                .filter(f -> f.getFieldName().equals(fieldName))
                .findFirst()
                .map(SortableField::getSqlColumn)
                .orElse(defaultColumn);

        // Validate direction
        String direction = DEFAULT_SORT_DIRECTION;
        if (parts.length > 1) {
            String dir = parts[1].trim().toUpperCase();
            if ("ASC".equals(dir) || "DESC".equals(dir)) {
                direction = dir;
            }
        }

        return new String[] { sqlColumn, direction };
    }

}
