package com.khahnm18.ecommercebackend.mapper.auth;

import com.khahnm18.ecommercebackend.constant.auth.AccountStatus;
import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.MyProfileAdminRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminUpdateRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
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
            .status(request.status() == null ? AccountStatus.ACTIVE : request.status())
            .build();
    }

    public void updateAccount(Account account, AdminUpdateRequest request) {
        if (request.email() != null) {
            account.setEmail(account.getEmail());
        }
        if (request.phoneNumber() != null) {
            account.setPhoneNumber(account.getPhoneNumber());
        }
        if (request.status() != null) {
            account.setStatus(account.getStatus());
        }
    }

    public void updateAccount(Account account, MyProfileAdminRequest request) {
        if (request.email() != null) {
            account.setEmail(account.getEmail());
        }
        if (request.phoneNumber() != null) {
            account.setPhoneNumber(account.getPhoneNumber());
        }
    }

    public Account toAccountFromUser(UserCreationRequest request) {
        if (request == null) return null;
        return Account.builder()
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .status(request.status() == null ? AccountStatus.ACTIVE : request.status())
            .build();
    }

    public void updateAccountFromUser(Account account, UserUpdateRequest request) {
        if (request.email() != null) {
            account.setEmail(request.email());
        }
        if (request.phoneNumber() != null) {
            account.setPhoneNumber(request.phoneNumber());
        }
        if (request.status() != null) {
            account.setStatus(request.status());
        }
    }

}
