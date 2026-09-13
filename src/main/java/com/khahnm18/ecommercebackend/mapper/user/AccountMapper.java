package com.khahnm18.ecommercebackend.mapper.user;

import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    public Account toAccount(AdminCreationRequest request) {
        if (request == null) return null;
        return Account.builder()
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .password(request.password())
            .status(request.status())
            .build();
    }

}
