package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.dto.request.auth.RoleSearchRequest;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.UUID;

public interface CustomRoleRepository {

    Page<Role> search(RoleSearchRequest request, Pageable pageable);

    List<Role> findRolesByAccountId(UUID accountId);

}
