package com.khahnm04.ecommerce.dto.projection.user;

import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmployeeProjection {

    Long getId();

    String getCode();

    String getJobTitle();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

    Long getUserId();

    String getUserEmail();

    String getUserPhoneNumber();

    LocalDate getUserDateOfBirth();

    String getUserFullName();

    String getUserAvatar();

    Gender getUserGender();

    UserStatus getUserStatus();

    String getCreatedByFullName();

    String getUpdatedByFullName();

}
