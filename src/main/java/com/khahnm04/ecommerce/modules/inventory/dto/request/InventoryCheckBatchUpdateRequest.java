package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckBatchUpdateRequest implements Serializable {

    @NotEmpty(message = "INVENTORY_CHECK_BATCH_EMPTY")
    @Valid
    private List<BatchItem> items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchItem implements Serializable {

        @NotNull(message = "INVENTORY_CHECK_DETAIL_ID_REQUIRED")
        private Long detailId;

        @NotNull(message = "INVENTORY_CHECK_ACTUAL_QTY_REQUIRED")
        private Integer actualQuantity;

        private String reason;
    }

}
