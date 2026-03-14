package com.khahnm04.ecommerce.modules.product.entity;

import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.brand.entity.Brand;
import com.khahnm04.ecommerce.modules.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Builder.Default
    @Column(name = "warranty_months")
    private Integer warrantyMonths = 12;

    @Builder.Default
    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "old_price", precision = 15, scale = 2)
    private BigDecimal oldPrice;

    @Column(name = "thumbnail", nullable = false, length = 500)
    private String thumbnail;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.ACTIVE;

}
