package com.khahnm04.ecommerce.modules.user.entity;

import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends BaseEntity<Long> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "job_title")
    private String jobTitle;

}
