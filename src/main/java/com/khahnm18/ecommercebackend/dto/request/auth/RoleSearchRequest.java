package com.khahnm18.ecommercebackend.dto.request.auth;

import java.time.Instant;

public record RoleSearchRequest(

    Boolean isDeleted,
    String keyword,
    Instant startDate,
    Instant endDate

) {}
