package com.khahnm04.ecommerce.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class PaginationUtil {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    private PaginationUtil() {}

    public static Pageable createPageable(
        Integer page,
        Integer size,
        String sort
    ) {
        int normalizedPage = (page == null || page < 1) ? DEFAULT_PAGE : page;
        int normalizedSize = (size == null || size < 1) ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);

        return PageRequest.of(
            normalizedPage - 1,
            normalizedSize,
            SortUtil.parseSort(sort)
        );
    }

}
