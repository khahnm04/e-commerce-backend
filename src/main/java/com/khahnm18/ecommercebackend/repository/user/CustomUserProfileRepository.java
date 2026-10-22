package com.khahnm18.ecommercebackend.repository.user;

import com.khahnm18.ecommercebackend.dto.request.user.UserSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.user.UserProfileAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserProfileRepository {

    Page<UserProfileAccount> search(UserSearchRequest request, Pageable pageable);

}
