package com.khahnm04.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag_groups")
public class TagGroup extends BaseEntity<Long> {

    @OneToMany(mappedBy = "tagGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    
}
