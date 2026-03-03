package com.khahnm04.ecommerce.modules.user.entity;

import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.branch.entity.Branch;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "job_title")
    private String jobTitle;

}
