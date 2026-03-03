package com.khahnm04.ecommerce.modules.user.dto.request;

import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.modules.user.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest implements Serializable {

    @NotBlank(message = "ADDRESS_RECEIVER_NAME_REQUIRED")
    private String receiverName;

    @NotBlank(message = "ADDRESS_RECEIVER_PHONE_REQUIRED")
    @ValidPhoneNumber
    private String receiverPhone;

    private Integer provinceCode;

    @NotBlank(message = "ADDRESS_PROVINCE_NAME_REQUIRED")
    private String provinceName;

    private Integer districtCode;

    @NotBlank(message = "ADDRESS_DISTRICT_NAME_REQUIRED")
    private String districtName;

    private String wardCode;

    @NotBlank(message = "ADDRESS_WARD_NAME_REQUIRED")
    private String wardName;

    @NotBlank(message = "ADDRESS_DETAIL_REQUIRED")
    private String detailAddress;

    private AddressType addressType;

    private String addressName;

    @Builder.Default
    private Boolean isDefault = false;

}
