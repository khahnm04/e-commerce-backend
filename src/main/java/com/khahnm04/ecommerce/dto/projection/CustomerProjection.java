package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CustomerProjection {
  Long getId();

  Long getUserId();

  String getFullName();

  String getEmail();

  String getPhoneNumber();

  LocalDate getDateOfBirth();

  Gender getGender();

  String getAvatar();

  UserStatus getUserStatus();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  LocalDateTime getDeletedAt();

  String getCreatedByFullName();

  String getUpdatedByFullName();
}
