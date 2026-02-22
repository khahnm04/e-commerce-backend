package com.khahnm04.ecommerce.modules.branch.dto.request;

import com.khahnm04.ecommerce.common.validation.phone.ValidPhoneNumber;
import com.khahnm04.ecommerce.modules.branch.enums.BranchStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequest implements Serializable {

    @NotBlank(message = "BRANCH_CODE_REQUIRED")
    private String code;

    @NotBlank(message = "BRANCH_NAME_REQUIRED")
    private String name;

    @NotBlank(message = "BRANCH_ADDRESS_DETAIL_REQUIRED")
    private String addressDetail;

    private String ward;

    @NotBlank(message = "BRANCH_DISTRICT_REQUIRED")
    private String district;

    @NotBlank(message = "BRANCH_CITY_REQUIRED")
    private String city;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @ValidPhoneNumber
    @NotBlank(message = "BRANCH_PHONE_REQUIRED")
    private String phoneNumber;

    private String email;

    private String openingHours;

    private BranchStatus status;

}
