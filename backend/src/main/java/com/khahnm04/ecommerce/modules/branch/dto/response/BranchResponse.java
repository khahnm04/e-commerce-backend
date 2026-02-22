package com.khahnm04.ecommerce.modules.branch.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.branch.enums.BranchStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse extends BaseResponse<Long> {

    private String code;
    private String name;
    private String addressDetail;
    private String ward;
    private String district;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phoneNumber;
    private String email;
    private String openingHours;
    private BranchStatus status;

}
