package com.khahnm04.ecommerce.entity;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategory> productCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryFilter> categoryFilters;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "show_in_menu")
    private Boolean showInMenu;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "position")
    @Builder.Default
    private Integer position = 0;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus status = CategoryStatus.ACTIVE;

}
