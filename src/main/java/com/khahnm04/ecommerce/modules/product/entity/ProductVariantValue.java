package com.khahnm04.ecommerce.modules.product.entity;

import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variant_values", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "product_variant_id", "variant_value_id" })
})
public class ProductVariantValue extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_value_id", nullable = false)
    private VariantValue variantValue;

}
