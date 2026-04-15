package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartResponse(

    Long id,
    List<CartItemResponse> items,
    int totalItems,
    BigDecimal totalAmount

) implements Serializable {
}
