package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khahnm04.ecommerce.common.enums.AddressType;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressResponse(

        Long id,
        Long customerId,
        String receiverName,
        String receiverPhone,
        String provinceCode,
        String provinceName,
        String districtCode,
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
