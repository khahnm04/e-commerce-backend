package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import org.springframework.data.domain.Page;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse getRoleDetailById(Long id, boolean isDeleted);

    Page<RoleResponse> getAllRoles(RoleFilterRequest filter);

    RoleResponse updateRole(Long id, RoleRequest request);

    void softDeleteRole(Long id);

    void deleteRole(Long id);

    void restoreRole(Long id);

    void bulkAction(BulkActionRequest request);

}
