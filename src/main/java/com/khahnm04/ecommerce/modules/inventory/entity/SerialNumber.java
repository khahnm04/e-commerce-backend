package com.khahnm04.ecommerce.modules.inventory.entity;

import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "serial_numbers")
@SQLDelete(sql = "UPDATE serial_numbers SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class SerialNumber extends BaseEntity<Long> {

    @Column(name = "serial_code", nullable = false, unique = true)
    private String serialCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private SerialNumberStatus status = SerialNumberStatus.IN_STOCK;

    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "sold_price", precision = 15, scale = 2)
    private BigDecimal soldPrice;

    @Column(name = "sold_date")
    private LocalDateTime soldDate;

    @Column(name = "warranty_start_date")
    private LocalDate warrantyStartDate;

    @Column(name = "warranty_end_date")
    private LocalDate warrantyEndDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}
