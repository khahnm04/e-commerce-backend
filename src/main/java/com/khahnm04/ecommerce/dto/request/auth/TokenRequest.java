package com.khahnm04.ecommerce.dto.request.auth;

import com.khahnm04.ecommerce.entity.auth.UserType;
import java.io.Serializable;
import lombok.Builder;

@Builder
public record TokenRequest(

    Long userId,
    String subject,
    String scope,
    UserType userType,
    boolean rememberMe

) implements Serializable {
}
