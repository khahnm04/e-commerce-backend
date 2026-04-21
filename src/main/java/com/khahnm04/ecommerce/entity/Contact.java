package com.khahnm04.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact extends BaseEntity<Long> {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

}
