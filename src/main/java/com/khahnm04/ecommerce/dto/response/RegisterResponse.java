package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterResponse(

    String fullName,
    LocalDate dateOfBirth,
    String email,
    String phoneNumber

) implements Serializable {
}
