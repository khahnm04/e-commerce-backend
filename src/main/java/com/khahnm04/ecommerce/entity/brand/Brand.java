package com.khahnm04.ecommerce.entity.brand;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import com.khahnm04.ecommerce.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "logo")
    private String logo;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BrandStatus status = BrandStatus.ACTIVE;

}
