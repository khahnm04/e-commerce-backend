package com.khahnm04.ecommerce.modules.inventory.entity;

import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory_check_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "inventory_check_id", "product_variant_id" })
})
public class InventoryCheckDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_check_id", nullable = false)
    private InventoryCheck inventoryCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(name = "system_quantity", nullable = false)
    @Builder.Default
    private Integer systemQuantity = 0;

    @Column(name = "actual_quantity", nullable = false)
    @Builder.Default
    private Integer actualQuantity = 0;

    @Column(name = "difference", nullable = false)
    @Builder.Default
    private Integer difference = 0;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

}
