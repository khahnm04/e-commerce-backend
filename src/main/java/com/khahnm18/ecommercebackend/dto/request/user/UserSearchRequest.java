package com.khahnm18.ecommercebackend.dto.request.user;

import com.khahnm18.ecommercebackend.enums.AccountStatus;
import java.time.Instant;

public record UserSearchRequest(
    Boolean isDeleted,
    String keyword,
    Instant startDate,
    Instant endDate,
    AccountStatus status
) {}
