package com.khahnm04.ecommerce.dto.request;

import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ContactRequest(

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "INVALID_EMAIL_FORMAT")
    String email

) implements Serializable {
}
