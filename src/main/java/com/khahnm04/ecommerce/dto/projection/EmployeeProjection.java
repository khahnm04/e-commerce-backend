package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.Gender;
import com.khahnm04.ecommerce.common.enums.EmployeeStatus;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmployeeProjection {

    Long getId();
    String getCode();
    String getJobTitle();
    String getEmail();
    String getPhoneNumber();
    LocalDate getDateOfBirth();
    String getFullName();
    String getAvatar();
    Gender getGender();
    EmployeeStatus getStatus();
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
