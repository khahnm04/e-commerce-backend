package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import java.time.Instant;

public interface AccountAdminProjection {

    Long getId();
    String getCode();
    String getFullName();
    String getJobTitle();
    String getEmail();
    String getPhoneNumber();
    Instant getDateOfBirth();
    String getAvatar();
    Gender getGender();
    AccountAdminStatus getStatus();
    Instant getLastLoginAt();
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getUpdatedAt();
    String getUpdatedBy();
    Instant getDeletedAt();
    String getDeletedBy();

}
