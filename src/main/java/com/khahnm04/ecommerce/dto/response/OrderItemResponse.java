package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderItemResponse(

    Long productVariantId,
    String productName,
    String sku,
    String image,
    BigDecimal unitPrice,
    Long quantity

) implements Serializable {
}
