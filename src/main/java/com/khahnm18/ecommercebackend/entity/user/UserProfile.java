package com.khahnm18.ecommercebackend.entity.user;

import com.khahnm18.ecommercebackend.enums.GenderType;
import com.khahnm18.ecommercebackend.entity.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender")
    private GenderType gender = GenderType.OTHER;

    @Builder.Default
    @Column(name = "total_point")
    private Integer totalPoint = 0;

    @Builder.Default
    @Column(name = "used_point")
    private Integer usedPoint = 0;

}
