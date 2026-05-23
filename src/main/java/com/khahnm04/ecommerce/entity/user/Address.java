package com.khahnm04.ecommerce.entity.user;

import com.khahnm04.ecommerce.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "province_code", length = 50)
    private String provinceCode;

    @Column(name = "province_name", nullable = false)
    private String provinceName;

    @Column(name = "district_code", length = 50)
    private String districtCode;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(name = "ward_code", length = 50)
    private String wardCode;

    @Column(name = "ward_name", nullable = false)
    private String wardName;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    @Builder.Default
    private AddressType addressType = AddressType.HOME;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

}
