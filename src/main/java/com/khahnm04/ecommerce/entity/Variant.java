package com.khahnm04.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variants")
public class Variant extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    private String name;

}
