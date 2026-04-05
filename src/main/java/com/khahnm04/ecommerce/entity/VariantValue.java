package com.khahnm04.ecommerce.entity;

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

    @Column(name = "value", nullable = false)
    private String value;

}
