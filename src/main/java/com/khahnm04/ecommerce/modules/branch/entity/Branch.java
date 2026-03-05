package com.khahnm04.ecommerce.modules.branch.entity;

import com.khahnm04.ecommerce.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branches")
public class Branch extends BaseEntity<Long> {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Column(name = "ward")
    private String ward;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "latitude", precision = 15, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 15, scale = 2)
    private BigDecimal longitude;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "opening_hours")
    private String openingHours;

}
