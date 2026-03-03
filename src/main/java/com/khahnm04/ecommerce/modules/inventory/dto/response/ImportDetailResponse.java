package com.khahnm04.ecommerce.modules.inventory.dto.response;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetailResponse implements Serializable {

    private Long id;
    private Long productVariantId;
    private String productVariantSku;
    private String productVariantName;
    private Long serialNumberId;
    private String serialCode;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;

}
