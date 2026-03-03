package com.khahnm04.ecommerce.modules.inventory.dto.response;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetailResponse implements Serializable {

    private Long id;
    private Long serialNumberId;
    private String serialCode;
    private Long productVariantId;
    private String productVariantSku;
    private String productVariantName;
    private LocalDateTime createdAt;

}
