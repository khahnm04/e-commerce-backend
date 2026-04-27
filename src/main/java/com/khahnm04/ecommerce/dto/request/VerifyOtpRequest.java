package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record VerifyOtpRequest(

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "Email không đúng định dạng")
    String email,

    @NotBlank(message = "OTP không được để trống")
    String otp

) implements Serializable {
}
