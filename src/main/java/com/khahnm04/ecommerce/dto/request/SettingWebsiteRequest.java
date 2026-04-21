package com.khahnm04.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record SettingWebsiteRequest (

    @NotBlank(message = "SETTING_WEBSITE_NAME_REQUIRED")
    String websiteName,

    @NotBlank(message = "SETTING_PHONE_NUMBER_REQUIRED")
    String phoneNumber,

    @NotBlank(message = "SETTING_EMAIL_REQUIRED")
    String email,

    @NotBlank(message = "SETTING_ADDRESS_REQUIRED")
    String address,

    @NotBlank(message = "SETTING_LOGO_REQUIRED")
    String logo,

    @NotBlank(message = "SETTING_FAVICON_REQUIRED")
    String favicon

) implements Serializable {
}
