package com.khahnm04.ecommerce.modules.catalog.entity;

import com.khahnm04.ecommerce.common.entity.BaseEntity;
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

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

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

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "meta_keywords")
    private String metaKeywords;

}
