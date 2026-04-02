package com.khahnm04.ecommerce.entity.user;

import com.khahnm04.ecommerce.entity.BaseEntity;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.entity.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends BaseEntity<Long> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

}
