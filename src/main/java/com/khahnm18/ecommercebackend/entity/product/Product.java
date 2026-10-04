package com.khahnm18.ecommercebackend.entity.product;

import com.khahnm18.ecommercebackend.constant.product.ProductStatus;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

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
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "images", columnDefinition = "jsonb")
    private List<String> images = new ArrayList<>();

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specifications", columnDefinition = "jsonb")
    private Map<String, Object> specifications = new HashMap<>();

    @Builder.Default
    @Column(name = "view", nullable = false)
    private Integer view = 0;

    @Builder.Default
    @Column(name = "rating_avg", nullable = false, precision = 3, scale = 2)
    private BigDecimal ratingAvg = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

}
