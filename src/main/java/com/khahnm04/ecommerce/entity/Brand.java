package com.khahnm04.ecommerce.entity;

import com.khahnm04.ecommerce.common.enums.BrandStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand extends BaseEntity<Long> {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo")
    private String logo;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BrandStatus status = BrandStatus.ACTIVE;

}
