package com.khahnm04.ecommerce.modules.product.entity;

import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attributes")
public class Attribute extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    private String name;

}
