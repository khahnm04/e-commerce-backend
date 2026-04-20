package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CustomerProjection {

    Long getId();

    @Value("#{target.user.id}")
    Long getUserId();

    @Value("#{target.user.fullName}")
    String getFullName();

    @Value("#{target.user.email}")
    String getEmail();

    @Value("#{target.user.phoneNumber}")
    String getPhoneNumber();

    @Value("#{target.user.dateOfBirth}")
    LocalDate getDateOfBirth();

    @Value("#{target.user.gender}")
    Gender getGender();

    @Value("#{target.user.avatar}")
    String getAvatar();

    @Value("#{target.user.status}")
    UserStatus getUserStatus();

    @Value("#{target.user.lastLoginAt}")
    LocalDateTime getLastLoginAt();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

    @Value("#{target.createdBy != null ? target.createdBy.fullName : null}")
    String getCreatedByFullName();

    @Value("#{target.updatedBy != null ? target.updatedBy.fullName : null}")
    String getUpdatedByFullName();

    @Value("#{target.deletedBy != null ? target.deletedBy.fullName : null}")
    String getDeletedByFullName();

}
