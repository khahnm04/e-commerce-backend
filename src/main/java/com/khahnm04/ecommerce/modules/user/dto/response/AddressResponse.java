package com.khahnm04.ecommerce.modules.user.dto.response;

import com.khahnm04.ecommerce.modules.user.enums.AddressType;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse implements Serializable {

    private Long id;
    private Long customerId;
    private String receiverName;
    private String receiverPhone;
    private Integer provinceCode;
    private String provinceName;
    private Integer districtCode;
    private String districtName;
    private String wardCode;
    private String wardName;
    private String detailAddress;
    private AddressType addressType;
    private String addressName;
    private Boolean isDefault;
    private String fullAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
