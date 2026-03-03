package com.khahnm04.ecommerce.modules.inventory.entity;

import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.inventory.enums.SupplierStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suppliers")
@SQLDelete(sql = "UPDATE suppliers SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Supplier extends BaseEntity<Long> {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "payment_terms")
    private String paymentTerms;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private SupplierStatus status = SupplierStatus.ACTIVE;

}
