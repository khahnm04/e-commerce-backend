package com.khahnm18.ecommercebackend.service.auth;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleRequest;
import com.khahnm18.ecommercebackend.dto.request.auth.RoleSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.auth.RoleResponse;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface RoleService {

    RoleResponse createRole(RoleRequest reqquest);

    RoleResponse getRoleDetailById(UUID id, boolean isDeleted);

    Page<RoleResponse> getAllRoles(int page, int size, RoleSearchRequest request);

    RoleResponse updateRole(UUID id, RoleRequest request);

    void softDeleteRole(UUID id);

    void deleteRole(UUID id);

    void restoreRole(UUID id);

    void bulkAction(BulkActionRequest request);

}
