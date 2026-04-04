package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.auth.RoleFilterRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse getRoleDetail(Long id, boolean isDeleted);

    PageResponse<RoleResponse> getAllRoles(RoleFilterRequest filter);

    PageResponse<RoleResponse> getAllDeletedRoles(RoleFilterRequest filter);

    RoleResponse updateRole(Long id, RoleRequest request);

    void softDeleteRole(Long id);

    void deleteRole(Long id);

    void restoreRole(Long id);

    void handleBulkAction(BulkActionRequest request);

}
