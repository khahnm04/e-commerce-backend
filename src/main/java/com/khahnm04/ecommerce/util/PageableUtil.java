package com.khahnm04.ecommerce.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtil {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    private PageableUtil() {}

    public static Pageable createPageable(int page, int size, String sort) {
        int normalizedPage = (page < 1) ? DEFAULT_PAGE : page;
        int normalizedSize = (size < 1) ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        return PageRequest.of(normalizedPage - 1, normalizedSize, parseSort(sort));
    }

    public static Sort parseSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.unsorted();
        }
        String[] parts = sort.trim().split(",");
        String property = parts[0].trim();
        Sort.Direction direction =
            (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(direction, property);
    }

}
