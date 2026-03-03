package com.khahnm04.ecommerce.modules.auth.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleBulkActionRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleFilterRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse getRoleDetailById(Long id);

    RoleResponse getDeletedRoleDetailById(Long id);

    PageResponse<RoleResponse> getAllRoles(RoleFilterRequest filter);

    PageResponse<RoleResponse> getAllDeletedRoles(RoleFilterRequest filter);

    RoleResponse updateRole(Long id, RoleRequest request);

    void softDeleteRole(Long id);

    void restoreRole(Long id);

    void handleBulkAction(RoleBulkActionRequest request);

}
