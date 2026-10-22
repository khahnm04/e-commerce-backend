package com.khahnm18.ecommercebackend.dto.request.website;

import jakarta.validation.constraints.NotBlank;

public record WebsiteRequest(

    @NotBlank(message = "website-info.name.required")
    String name,

    @NotBlank(message = "website-info.phoneNumber.required")
    String phoneNumber,

    @NotBlank(message = "website-info.email.required")
    String email,

    @NotBlank(message = "website-info.address.required")
    String address,

    @NotBlank(message = "website-info.logo.required")
    String logo,

    @NotBlank(message = "website-info.favicon.required")
    String favicon

) {}
