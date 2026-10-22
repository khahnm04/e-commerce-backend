package com.khahnm18.ecommercebackend.repository.user;

import com.khahnm18.ecommercebackend.dto.request.user.AdminSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.user.AdminProfileAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomAdminProfileRepository {

    Page<AdminProfileAccount> search(AdminSearchRequest request, Pageable pageable);

}
