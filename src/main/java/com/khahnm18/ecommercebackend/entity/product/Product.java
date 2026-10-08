package com.khahnm18.ecommercebackend.entity.product;

import com.khahnm18.ecommercebackend.enums.ProductStatus;
import com.khahnm18.ecommercebackend.entity.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "version_id", nullable = false)
    private UUID versionId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "sku", unique = true)
    private String sku;

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Builder.Default
    @Column(name = "price_old", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceOld = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "price_new", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceNew = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "discount", nullable = false)
    private Integer discount = 0;

    @Builder.Default
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "avatar", nullable = false, length = 500)
    private String avatar;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

}
