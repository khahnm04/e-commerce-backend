package com.khahnm18.ecommercebackend.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import java.util.List;

public record ProductOptionRequest(

    @NotBlank
    String name,

    @Valid
    @NotEmpty
    List<ProductOptionValueRequest> values

) {}
