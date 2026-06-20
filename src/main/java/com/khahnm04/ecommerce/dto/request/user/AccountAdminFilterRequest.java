package com.khahnm04.ecommerce.dto.request.user;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import lombok.Builder;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record AccountAdminFilterRequest(

    Integer page,
    Integer size,
    Boolean isDeleted,
    String sort,
    String keyword,
    Instant startDate,
    Instant endDate,
    AccountAdminStatus status,
    Gender gender

) implements Serializable {
    public AccountAdminFilterRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (sort == null) sort = "createdAt,desc";
        if (isDeleted == null) isDeleted = false;
    }
}
