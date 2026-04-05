package com.khahnm04.ecommerce.dto.request;

import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.common.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record AddressRequest(

    @NotBlank(message = "ADDRESS_RECEIVER_NAME_REQUIRED")
    String receiverName,

    @NotBlank(message = "ADDRESS_RECEIVER_PHONE_REQUIRED")
    @ValidPhoneNumber
    String receiverPhone,

    Integer provinceCode,

    @NotBlank(message = "ADDRESS_PROVINCE_NAME_REQUIRED")
    String provinceName,

    Integer districtCode,

    @NotBlank(message = "ADDRESS_DISTRICT_NAME_REQUIRED")
    String districtName,

    String wardCode,

    @NotBlank(message = "ADDRESS_WARD_NAME_REQUIRED")
    String wardName,

    @NotBlank(message = "ADDRESS_DETAIL_REQUIRED")
    String detailAddress,

    AddressType addressType,
    String addressName,
    Boolean isDefault

) implements Serializable {

    public AddressRequest {
        if (isDefault == null) isDefault = false;
    }

}
