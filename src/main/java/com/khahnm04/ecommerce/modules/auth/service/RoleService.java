package com.khahnm04.ecommerce.modules.auth.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import java.util.Set;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleFilterRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.RoleRequest;
import com.khahnm04.ecommerce.modules.auth.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse getRoleDetail(Long id, boolean isDeleted);

    PageResponse<RoleResponse> getAllRoles(RoleFilterRequest filter);

    PageResponse<RoleResponse> getAllDeletedRoles(RoleFilterRequest filter);

    RoleResponse updateRole(Long id, RoleRequest request);

    void softDeleteRole(Long id);

    void restoreRole(Long id);

    void handleBulkAction(Set<Long> roleIds, String action);

}
