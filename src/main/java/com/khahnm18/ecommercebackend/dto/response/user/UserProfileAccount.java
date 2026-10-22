package com.khahnm18.ecommercebackend.dto.response.user;

import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.user.UserProfile;

public record UserProfileAccount(

    UserProfile profile,
    Account account

) {}
