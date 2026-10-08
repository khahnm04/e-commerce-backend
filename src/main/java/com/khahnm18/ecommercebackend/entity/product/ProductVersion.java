package com.khahnm18.ecommercebackend.entity.product;

import com.khahnm18.ecommercebackend.entity.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_versions")
public class ProductVersion extends BaseEntity {

    @Column(name = "group_id", nullable = false)
    private UUID groupId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @Builder.Default
    @Column(name = "rating_avg", nullable = false, precision = 3, scale = 2)
    private BigDecimal ratingAvg = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @Builder.Default
    @Column(name = "price_old", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceOld = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "price_new", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceNew = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "total_stock", nullable = false)
    private Integer totalStock = 0;

    @Builder.Default
    @Column(name = "sold_count", nullable = false)
    private Integer soldCount = 0;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specifications", columnDefinition = "jsonb", nullable = false)
    private List<SpecificationGroup> specifications = new ArrayList<>();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

}
