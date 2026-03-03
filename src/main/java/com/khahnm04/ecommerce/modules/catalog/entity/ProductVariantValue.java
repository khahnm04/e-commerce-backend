package com.khahnm04.ecommerce.modules.catalog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variant_values", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "product_variant_id", "variant_value_id" })
})
public class ProductVariantValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_value_id", nullable = false)
    private VariantValue variantValue;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
