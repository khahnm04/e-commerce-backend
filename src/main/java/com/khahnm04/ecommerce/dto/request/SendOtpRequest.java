package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record SendOtpRequest(

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "Email không đúng định dạng")
    String email

) implements Serializable {
}
