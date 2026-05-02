package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.enums.CustomerStatus;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CustomerProjection {

    Long getId();
    String getFullName();
    String getEmail();
    String getPhoneNumber();
    LocalDate getDateOfBirth();
    Gender getGender();
    String getAvatar();
    CustomerStatus getStatus();
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

    @Value("#{target.userProviders != null ? target.userProviders.stream().map(p -> p.provider.name()).collect(java.util.stream.Collectors.toList()) : null}")
    java.util.List<String> getProviders();

}
