package com.khahnm04.ecommerce.modules.user.dto.response;

import com.khahnm04.ecommerce.modules.user.enums.AddressType;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record AddressResponse(

    Long id,
    Long customerId,
    String receiverName,
    String receiverPhone,
    Integer provinceCode,
    String provinceName,
    Integer districtCode,
    String districtName,
    String wardCode,
    String wardName,
    String detailAddress,
    AddressType addressType,
    String addressName,
    Boolean isDefault,
    String fullAddress,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) implements Serializable {
}
