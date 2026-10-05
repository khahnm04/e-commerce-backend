package com.khahnm18.ecommercebackend.dto.request.product;

import jakarta.validation.constraints.NotBlank;

public record ProductOptionValueRequest(

    @NotBlank
    String label,

    @NotBlank
    String value

) {}
