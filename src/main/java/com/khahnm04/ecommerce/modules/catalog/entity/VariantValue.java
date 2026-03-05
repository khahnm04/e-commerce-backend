package com.khahnm04.ecommerce.modules.catalog.entity;

import com.khahnm04.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variant_values", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "variant_id", "value" })
})
public class VariantValue extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;

    @Column(name = "code")
    private String code;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

}
