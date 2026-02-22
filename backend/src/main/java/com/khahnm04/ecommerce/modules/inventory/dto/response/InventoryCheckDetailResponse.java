package com.khahnm04.ecommerce.modules.inventory.dto.response;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckDetailResponse implements Serializable {

    private Long id;
    private Long productVariantId;
    private String productVariantSku;
    private String productVariantName;
    private Integer systemQuantity;
    private Integer actualQuantity;
    private Integer difference;
    private String reason;
    private LocalDateTime createdAt;

}
