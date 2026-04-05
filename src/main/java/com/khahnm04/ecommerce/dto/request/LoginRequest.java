package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.validation.email.ValidEmail;
import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record LoginRequest(

    @ValidPhoneNumber
    String phoneNumber,

    @ValidEmail
    String email,

    @NotBlank(message = "password cannot be blank")
    String password,

    Boolean rememberMe

) implements Serializable {

    public LoginRequest {
        if (rememberMe == null) rememberMe = false;
    }

}
