package com.khahnm18.ecommercebackend.entity.website;

import com.khahnm18.ecommercebackend.entity.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "website_infos")
public class WebsiteInfo extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "logo", nullable = false, length = 500)
    private String logo;

    @Column(name = "favicon", nullable = false, length = 500)
    private String favicon;

}
