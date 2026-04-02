package com.khahnm04.ecommerce.entity.category;

import com.khahnm04.ecommerce.entity.BaseEntity;
import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "position")
    @Builder.Default
    private Integer position = 0;

    @Column(name = "path")
    private String path;

    @Column(name = "level")
    @Builder.Default
    private Integer level = 0;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus status = CategoryStatus.ACTIVE;

}
